package cn.dreamjun.my.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.SubComment;
import cn.dreamjun.my.service.ISubCommentService;
import io.swagger.annotations.Api;
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
@Api(tags = "【门户】子评论接口")
public class SubCommentController {

    @Autowired
    private ISubCommentService subCommentService;

    @ApiOperation("发表子评论")
    @PostMapping("/sub-comment")
    public R postSubComment(@RequestBody SubComment subComment) {
        return subCommentService.addSubComment(subComment);
    }

    @ApiOperation("删除子评论")
    @DeleteMapping("/sub-comment/{subCommentId}")
    public R deleteSubComment(@PathVariable("subCommentId") String subCommentId) {
        return subCommentService.removeSubComment(subCommentId);
    }
}

