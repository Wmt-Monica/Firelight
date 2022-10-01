package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.Topic;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
public interface ITopicService extends IService<Topic> {

    R addTopic(Topic topic);

    R listTopics();

    R updateTopic(String topicId, Topic topic);

    R deleteTopic(String topicId);

    boolean checkTopic(String topicId);

    R listFollowTopics();

    R listTopicMenu(int count);

    R listSimpleTopic();
}
