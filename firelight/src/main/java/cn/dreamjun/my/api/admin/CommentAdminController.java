package cn.dreamjun.my.api.admin;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.service.ICommentAdminService;
import cn.dreamjun.my.service.ISubCommentAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
@Api(tags = "【管理中心】管理评论")
@RestController
@PreAuthorize("@userRemote.adminPermission()")
public class CommentAdminController {

    @Autowired
    private ICommentAdminService adminService;

    @Autowired
    private ISubCommentAdminService subCommentAdminService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码，从1开始."),
            @ApiImplicitParam(name = "myId", value = "动态ID."),
            @ApiImplicitParam(name = "id", value = "评论ID."),
            @ApiImplicitParam(name = "keyword", value = "关键字."),
            @ApiImplicitParam(name = "userId", value = "用户ID."),
    })
    @ApiOperation("获取评论列表")
    @GetMapping("admin/comment/list/{page}")
    public R getCommentList(@PathVariable("page") int page,
                            @RequestParam(value = "myId", required = false) String myId,
                            @RequestParam(value = "id", required = false) String id,
                            @RequestParam(value = "keyword", required = false) String keyword,
                            @RequestParam(value = "userId", required = false) String userId) {
        return adminService.listComment(page, myId, id, keyword, userId);
    }

    @ApiImplicitParam(name = "commentId", value = "评论ID")
    @ApiOperation("获取子评论列表")
    @GetMapping("admin/sub-comment/list/{commentId}")
    public R getSubCommentList(@PathVariable("commentId") String commentId) {
        return subCommentAdminService.listSubComments(commentId);
    }

    @ApiImplicitParam(name = "commentId", value = "评论ID")
    @ApiOperation("删除评论")
    @DeleteMapping("admin/comment/{commentId}")
    public R deleteComment(@PathVariable("commentId") String commentId) {
        return adminService.removeComment(commentId);
    }

    @ApiImplicitParam(name = "commentId", value = "评论ID")
    @ApiOperation("删除子评论")
    @DeleteMapping("admin/sub-comment/{commentId}")
    public R deleteSubComment(@PathVariable("commentId") String commentId) {
        return subCommentAdminService.removeSubComment(commentId);
    }

}


