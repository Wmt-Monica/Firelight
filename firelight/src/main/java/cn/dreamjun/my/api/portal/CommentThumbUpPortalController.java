package cn.dreamjun.my.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.service.ICommentThumbUpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
@Api(tags = "【门户】评论点赞/取消点赞接口")
@RestController
public class CommentThumbUpPortalController {

    @Autowired
    private ICommentThumbUpService commentThumbUpService;


    @ApiOperation("评论点赞/取消点赞")
    @ApiImplicitParam(name = "commentId", value = "评论 ID", required = true)
    @PostMapping("/comment/thumb-up/{commentId}")
    public R commentThumbUp(@PathVariable("commentId") String commentId) {
        return commentThumbUpService.doThumbUp(commentId);
    }
}

