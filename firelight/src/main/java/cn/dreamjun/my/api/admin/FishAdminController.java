package cn.dreamjun.my.api.admin;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.service.IFishAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname FishAdminController
 * @Description TODO
 * @Date 2022/9/22 17:59
 * @Created by 翊
 */
@Api(tags = "【管理中心】动态列表管理")
@RestController
@PreAuthorize("@userRemote.adminPermission()")
public class FishAdminController {

    @Autowired
    private IFishAdminService fishAdminService;

    @ApiImplicitParam(name = "fishId", value = "动态ID")
    @ApiOperation("删除动态")
    @DeleteMapping("/admin/firelight/{fishId}")
    public R deleteFish(@PathVariable("fishId") String fishId) {
        return fishAdminService.removeFish(fishId);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码，默认从1开始", required = true),
            @ApiImplicitParam(name = "topicId", value = "话题ID"),
            @ApiImplicitParam(name = "userId", value = "用户ID"),
            @ApiImplicitParam(name = "fishId", value = "动态ID"),
    })
    @ApiOperation("获取动态列表")
    @GetMapping("/admin/firelight/list/{page}")
    public R getFish(@PathVariable("page") int page,
                     @RequestParam(value = "topicId", required = false) String topId,
                     @RequestParam(value = "userId", required = false) String userId,
                     @RequestParam(value = "fishId", required = false) String fishId,
                     @RequestParam(value = "title", required = false) String title) {
        return fishAdminService.listFishes(page, topId, userId, fishId, title);
    }

}
