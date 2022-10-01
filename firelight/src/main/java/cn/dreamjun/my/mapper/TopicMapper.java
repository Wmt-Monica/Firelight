package cn.dreamjun.my.mapper;

import cn.dreamjun.my.pojo.Topic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
public interface TopicMapper extends BaseMapper<Topic> {

    List<Topic> listMyFollowTopics(String userId);

    List<Topic> listSimpleTopicLimitCount(int count);

    List<Topic> listMyUnFollowTopics(String userId, int count);
}
