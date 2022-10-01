package cn.dreamjun.my.api.admin;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.service.IImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(tags = "【管理中心】图片接口")
@PreAuthorize("@userRemote.adminPermission()")
public class ImageAdminController {

    @Autowired
    IImageService imageService;

    @Value("${qiniu.admin.category}")
    String adminCategory;

    @ApiOperation("上传图片")
    @PostMapping("/admin/image/upload")
    @ApiImplicitParam(name = "file", value = "文件")
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        return imageService.uploadFile(file, adminCategory);
    }

    @PostMapping("/admin/image/fileDelete")
    @ApiOperation("删除图片")
    @ApiImplicitParam(name = "fileUrl", value = "文件 url 路径")
    public R deleteFile(@RequestParam("fileUrl") String fileUrl) {
        return imageService.deleteFile(fileUrl);
    }
}

