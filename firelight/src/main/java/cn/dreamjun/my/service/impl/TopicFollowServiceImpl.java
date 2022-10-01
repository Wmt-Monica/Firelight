package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.my.base.MyBaseService;
import cn.dreamjun.my.pojo.TopicFollow;
import cn.dreamjun.my.mapper.TopicFollowMapper;
import cn.dreamjun.my.remote.UserRemote;
import cn.dreamjun.my.service.ITopicFollowService;
import cn.dreamjun.my.service.ITopicService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
@Service
public class TopicFollowServiceImpl extends MyBaseService<TopicFollowMapper, TopicFollow> implements ITopicFollowService {

    @Autowired
    private UserRemote userRemote;

    @Autowired
    ITopicService topicService;

    /**
     * 关注 / 取消关注话题
     * @param topicId
     * @return
     */
    @Override
    public R followTopic(String topicId) {
        try {
            // 检查用户是否登录
            UserVo user = getUser();
            if (user == null) {
                return R.NOT_LOGIN();
            }
            // 检查话题是否存在
            boolean isExist = topicService.checkTopic(topicId);
            if (!isExist) {
                return R.FAILED("话题不存在");
            }
            // 删除关注的话题
            // 删除成功-->返回取消关注成功
            // 删除失败，说明不存在，添加入库-->关注成功
            String userId = user.getId();
            QueryWrapper<TopicFollow> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            queryWrapper.eq("topic_id", topicId);
            boolean remove = remove(queryWrapper);
            if (remove) {
                return R.SUCCESS("已取消关注");
            }
            // 未关注，数据入库
            TopicFollow targetTopicFollow = new TopicFollow();
            targetTopicFollow.setUserId(userId);
            targetTopicFollow.setTopicId(topicId);
            save(targetTopicFollow);
            return R.SUCCESS("关注成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.FAILED("服务繁忙，请稍后重试");
        }
    }
}
