package cn.dreamjun.my.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.service.ITopicService;
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
@Api(tags = "【门户】话题接口")
@RestController
public class TopicPortalController {

    @Autowired
    private ITopicService topicService;

    @ApiOperation("获取所有的话题")
    @GetMapping("/portal/topic/list")
    public R listTopics() {
        return topicService.listTopics();
    }

    /**
     * 获取侧栏话题列表：
     * 关注的话题，如果关注的话题数量不够9个，那么就去获取其他的话题
     * 获取关注的数据，如果关注的数据不够，再获取非关注的，再补够
     */
    @ApiImplicitParam(name = "count", value = "数量")
    @ApiOperation("获取话题菜单列表")
    @GetMapping("/portal/topic/menu/{count}")
    public R listTopicMenu(@PathVariable("count") int count) {
        return topicService.listTopicMenu(count);
    }

    @ApiOperation("获取当前用户关注的话题列表")
    @GetMapping("/portal/topic/list-follow")
    public R listFollowTopics() {
        return topicService.listFollowTopics();
    }
}

