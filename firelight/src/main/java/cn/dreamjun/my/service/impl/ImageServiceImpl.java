package cn.dreamjun.my.service.impl;


import cn.dreamjun.base.reponse.R;
import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.common.utils.ImageType;
import cn.dreamjun.common.utils.MD5;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.my.mapper.ImageMapper;
import cn.dreamjun.my.pojo.Image;
import cn.dreamjun.my.remote.UserRemote;
import cn.dreamjun.my.service.IImageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 翊
 * @since 2022-09-15
 */
@Service
@Slf4j
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements IImageService, InitializingBean {

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    @Autowired
    private Auth auth;

    private StringMap putPolicy;

    @Value("${qiniu.path}")
    private String PATH;

    @Value("${qiniu.max-file-size}")
    private String MAX_FILE_SIZE;

    @Value("${qiniu.bucket}")
    private String BUCKET;

    @Autowired
    UserRemote userRemote;


    @Override
    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
    }

    @Override
    public R uploadFile(MultipartFile file, String category) throws QiniuException {
        UserVo user = null;
        try {
            user = userRemote.getUser();
        } catch (Exception ex) {
            return R.SERVER_BUSY();  // 远程调用服务业务繁忙错误
        }
        // 获取当前登录的用户为空，则返回未登录响应
        if (user == null) {
            return R.FAILED("用户未登录");
        }

        // 获取文件信息
        String originalFileName = file.getOriginalFilename();  // 原始文件名称
        String contentType = file.getContentType();  // 文件类型
        long mbSize = file.getSize() / 1024 / 1024;
        String md5 = "";
        try {
            md5 = MD5.createMd5(file.getInputStream());
        } catch (IOException ex) {
            return R.FAILED("图片上传失败");
        }
        // 比对 MD5 & category 数据库中是否已经存在，如果存在，那么就直接返回响应结果
        // 防止图片重复上传
        QueryWrapper<Image> imageQueryWrapper = new QueryWrapper<>();
        imageQueryWrapper.eq("md5", md5);
        imageQueryWrapper.eq("category", category);
        Image one = getOne(imageQueryWrapper);
        if (one != null) {
            return R.SUCCESS("图片上传成功", one.getPath());
        }

        // 检查文件类型是否支持
        if (TextUtils.isEmpty(checkImageType(contentType, originalFileName))) {
            return R.FAILED("不支持此文件类型");
        }
        // 检查文件大小是否超额
        if (mbSize > Integer.parseInt(MAX_FILE_SIZE)) {
            return R.FAILED("文件大不能超过 4 Mb");
        }

        // 获取文件流对象
        InputStream inputStream = null;
        Response response = null;
        try {
            inputStream = file.getInputStream();
            String key = category + IdWorker.getIdStr();
            response = uploadManager.put(inputStream, key, getToken(key),null,null);

            int trytimes = 0;
            while(response.needRetry() && trytimes < 3) {
                response = uploadManager.put(inputStream, key, getToken(key),null,null);
                trytimes ++;
            }
        } catch (IOException ex) {
            return R.FAILED("图片上传失败");
        }

        if (response.needRetry()) {
            return R.FAILED("图片上传失败");
        } else {
            DefaultPutRet putRst = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            String url = "/" + putRst.key;

            // 初始化图片信息
            Image image = new Image();
            image.setCategory(category);
            image.setMd5(md5);
            image.setSize(mbSize);
            image.setUserId(user.getId());  // 设置用户 ID
            image.setPath(url);  // 图片 URL 路径

            // 持久化图片对象
            save(image);

            // 返回响应
            return R.SUCCESS("图片上传成功", url);
        }
    }

    /**
     * @param fileUrl
     */
    @Override
    public R deleteFile(String fileUrl) {
        String key = fileUrl.substring(fileUrl.indexOf("/") + 1);
        try{
            bucketManager.delete(BUCKET,key);
        }catch(QiniuException ex){
            //如果遇到异常，说明删除失败
            log.error(ex.code() + "");
            log.error(ex.response.toString());
            return R.FAILED("图片删除失败");
        }
        return R.SUCCESS("图片删除成功");
    }

    private String getToken(String key){
        return this.auth.uploadToken(BUCKET, key,3600,putPolicy);
    }

    private String checkImageType(String contentType, String originalFilename) {
        if (originalFilename.endsWith(ImageType.TYPE_JPG) ||
                (ImageType.TYPE_PREFIX + "/" + ImageType.TYPE_JPG).equals(contentType)) {
            return ImageType.TYPE_JPG;
        }
        if (originalFilename.endsWith(ImageType.TYPE_JPEG) ||
                (ImageType.TYPE_PREFIX + "/" + ImageType.TYPE_JPEG).equals(contentType)) {
            return ImageType.TYPE_JPEG;
        }
        if (originalFilename.endsWith(ImageType.TYPE_PNG) ||
                (ImageType.TYPE_PREFIX + "/" + ImageType.TYPE_PNG).equals(contentType)) {
            return ImageType.TYPE_PNG;
        }
        if (originalFilename.endsWith(ImageType.TYPE_GIF) ||
                (ImageType.TYPE_PREFIX + "/" + ImageType.TYPE_GIF).equals(contentType)) {
            return ImageType.TYPE_GIF;
        }
        return null;
    }
}
