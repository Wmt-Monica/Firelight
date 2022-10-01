package cn.dreamjun.my.api.admin;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.Topic;
import cn.dreamjun.my.service.ITopicService;
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
@Api(tags = "【管理中心】话题接口")
@RestController
@PreAuthorize("@userRemote.adminPermission()")
public class TopicAdminController {

    @Autowired
    private ITopicService topicService;


    @ApiOperation("添加话题")
    @ApiImplicitParam(name = "topic", value = "话题数据Bean")
    @PostMapping("/admin/topic")
    public R addTopic(@RequestBody Topic topic) {
        return topicService.addTopic(topic);
    }

    @ApiOperation("删除话题")
    @ApiImplicitParam(name = "topicId", value = "删除指定ID的话题")
    @DeleteMapping("/admin/topic/{topicId}")
    public R deleteTopic(@PathVariable("topicId") String topicId) {
        return topicService.deleteTopic(topicId);
    }

    @ApiOperation("获取话题列表（详细列表）")
    @GetMapping("/admin/topic/list")
    public R listTopics() {
        return topicService.listTopics();
    }

    @ApiOperation(value = "获取话题列表（简略列表）",
            notes = "用于管理员在动态列表中查询中选择话题列表，只含有话题 ID 和 话题 Name")
    @GetMapping("/admin/topic/simple-list")
    public R listSimpleTopics() {
        return topicService.listSimpleTopic();
    }

    @ApiOperation("获取话题")
    @ApiImplicitParam(name = "topicId", value = "要获取话题详情的ID")
    @GetMapping("/admin/topic/{topicId}")
    public R getTopic(@PathVariable("topicId") String topicId) {
        return null;
    }

    @ApiOperation("更新话题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicId", value = "更新话题的ID"),
            @ApiImplicitParam(name = "topic", value = "话题数据Bean")
    })
    @PutMapping("/admin/topic/{topicId}")
    public R updateTopic(@PathVariable("topicId") String topicId, @RequestBody Topic topic) {
        return topicService.updateTopic(topicId, topic);
    }
}
