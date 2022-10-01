package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.Image;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qiniu.common.QiniuException;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 翊
 * @since 2022-09-15
 */
public interface IImageService extends IService<Image> {
    R uploadFile(MultipartFile file, String category) throws QiniuException;

    R deleteFile(String fileUrl);
}
