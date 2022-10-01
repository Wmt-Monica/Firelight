package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.TopicFollow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
public interface ITopicFollowService extends IService<TopicFollow> {

    R followTopic(String topicId);
}
