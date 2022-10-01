package cn.dreamjun.my.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.Comment;
import cn.dreamjun.my.service.ICommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
@RestController
@Api(tags = "【门户】评论接口")
public class CommentPortalController {

    @Autowired
    private ICommentService commentService;

    @ApiOperation("发表评论")
    @PostMapping("/comment")
    public R postComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }


    @ApiImplicitParam(name = "id", value = "评论ID", required = true)
    @ApiOperation("删除评论")
    @DeleteMapping("/comment/{id}")
    public R deleteComment(@PathVariable("id") String id) {
        return commentService.removeComment(id);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "myId", value = "动态ID", required = true),
            @ApiImplicitParam(name = "page", value = "页码，从1开始", required = true),
            @ApiImplicitParam(name = "sort", value = "排序，0默认，按时间，1按热度排序(点赞)")
    })
    @ApiOperation("获取评论列表")
    @GetMapping("/comment/list/{myId}/{page}")
    public R getComments(@PathVariable("myId") String myId, @PathVariable("page") int page,
                         @RequestParam(value = "sort", required = false) String sort) {
        return commentService.listComments(page, sort, myId);
    }
}

