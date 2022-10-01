package cn.dreamjun.my.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.service.IThumbUpService;
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
@Api(tags = "【门户】动态点赞")
@RestController
public class ThumbPortalUpController {

    @Autowired
    private IThumbUpService thumbUpService;

    @ApiImplicitParam(name = "fishId", value = "动态的ID")
    @ApiOperation("动态点赞/取消点赞")
    @PostMapping("/thumb-up/{fishId}")
    public R postThumbUp(@PathVariable("fishId") String fishId) {
        return thumbUpService.thumbUp(fishId);
    }

}
