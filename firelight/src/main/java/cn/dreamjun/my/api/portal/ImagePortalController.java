package cn.dreamjun.my.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.my.service.IImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 翊
 * @since 2022-09-15
 */
@RestController
@Api(tags = "【门户】图片接口")
public class ImagePortalController {

    @Autowired
    IImageService imageService;

    @ApiOperation("上传 FireFly 图片")
    @PostMapping("/portal/image-firelight/upload")
    @ApiImplicitParam(name = "file", value = "文件")
    public R uploadCover(@RequestParam("file") MultipartFile file) throws Exception {
        return imageService.uploadFile(file, Constants.ImageUploadCategory.PORTAL_FIREFLY_CATEGORY);
    }

    @ApiOperation("上传 Avatar 链接封面图片")
    @PostMapping("/portal/image-avatar/upload")
    @ApiImplicitParam(name = "file", value = "文件")
    public R uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        return imageService.uploadFile(file, Constants.ImageUploadCategory.PORTAL_AVATAR_CATEGORY);
    }

    @ApiOperation("上传 FireFly 封面图片")
    @PostMapping("/portal/image-cover/upload")
    @ApiImplicitParam(name = "file", value = "文件")
    public R uploadFirefly(@RequestParam("file") MultipartFile file) throws Exception {
        return imageService.uploadFile(file, Constants.ImageUploadCategory.PORTAL_COVER_CATEGORY);
    }

    @PostMapping("/portal/image/fileDelete")
    @ApiOperation("删除图片")
    @ApiImplicitParam(name = "fileUrl", value = "文件 url 路径")
    public R deleteFile(@RequestParam("fileUrl") String fileUrl) {
        return imageService.deleteFile(fileUrl);
    }
}

