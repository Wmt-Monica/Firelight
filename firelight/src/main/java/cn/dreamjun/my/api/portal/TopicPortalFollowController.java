package cn.dreamjun.my.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.service.ITopicFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@Api(tags = "【门户】话题关注")
public class TopicPortalFollowController {

    @Autowired
    ITopicFollowService topicFollowService;

    @ApiImplicitParam(name = "topicId", value = "要关注的话题ID")
    @ApiOperation(value = "关注话题/取消关注", notes = "需要账号已经登录")
    @PutMapping("/topic-follow/{topicId}")
    public R followTopic(@PathVariable("topicId") String topicId) {
        return topicFollowService.followTopic(topicId);
    }

}

