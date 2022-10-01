package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.my.pojo.Fish;
import cn.dreamjun.my.pojo.Topic;
import cn.dreamjun.my.mapper.TopicMapper;
import cn.dreamjun.my.pojo.TopicFollow;
import cn.dreamjun.my.remote.UserRemote;
import cn.dreamjun.my.service.IFishService;
import cn.dreamjun.my.service.ITopicFollowService;
import cn.dreamjun.my.service.ITopicService;
import cn.dreamjun.my.vo.TopicVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
@Service
@Slf4j
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements ITopicService {

    @Autowired
    private ITopicFollowService topicFollowService;

    @Autowired
    private IFishService fishService;

    @Autowired
    private UserRemote userRemote;


    /**
     * 新增话题
     * @param topic
     * @return
     */
    @Override
    public R addTopic(Topic topic) {
        //检查数据
        String name = topic.getName();
        if (TextUtils.isEmpty(name)) {
            return R.FAILED("话题名称不可以为空.");
        }
        String cover = topic.getCover();
        if (TextUtils.isEmpty(cover)) {
            return R.FAILED("话题不可以没有封面.");
        }
        String description = topic.getDescription();
        if (TextUtils.isEmpty(description)) {
            return R.FAILED("表输入话题介绍.");
        }
        //数据入库
        save(topic);
        //返回结果
        return R.SUCCESS("添加话题成功.");
    }

    /**
     * 获取话题的列表
     *
     * @return
     */
    @Override
    public R listTopics() {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("`order`");
        List<Topic> list = list(queryWrapper);
        //如果用户已经登录了
        //处理用户对话题的关注状态
        UserVo user = userRemote.getUser();
        if (user != null) {
            //获取当前用户的话题关注列表
            List<TopicFollow> followList = topicFollowService.query().eq("user_id", user.getId()).list();
            Map<String, TopicFollow> followMap = followList.stream().collect(Collectors.toMap(TopicFollow::getTopicId, item -> item));
            for (Topic topic : list) {
                String id = topic.getId();
                TopicFollow topicFollow = followMap.get(id);
                topic.setFollow(topicFollow != null);
            }
        }
        return R.SUCCESS("获取话题列表成功").setData(list);
    }

    /**
     * 获取含有部分数据的 TopicList 集合（只含有 id, name）
     * @return
     */
    @Override
    public R listSimpleTopic() {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name");
        List<Topic> list = list(queryWrapper);
        List<TopicVo> simpleTopicList = list.stream().map(item -> {
            TopicVo topicVo = new TopicVo();
            topicVo.setId(item.getId());
            topicVo.setName(item.getName());
            return topicVo; }).collect(Collectors.toList());
        return R.SUCCESS("获取话题列表成功.").setData(simpleTopicList);
    }

    /**
     * 更新话题
     *
     * @param topicId
     * @param topic
     * @return
     */
    @Override
    public R updateTopic(String topicId, Topic topic) {
        Topic dbTopic = getById(topicId);
        if (dbTopic == null) {
            return R.FAILED("话题不存在.");
        }
        String name = topic.getName();
        if (!TextUtils.isEmpty(name)) {
            dbTopic.setName(name);
        }
        String cover = topic.getCover();
        if (!TextUtils.isEmpty(cover)) {
            dbTopic.setCover(cover);
        }
        Integer order = topic.getOrder();
        if (order != null) {
            dbTopic.setOrder(order);
        }
        String description = topic.getDescription();
        if (!TextUtils.isEmpty(description)) {
            dbTopic.setDescription(description);
        }
        updateById(dbTopic);
        return R.SUCCESS("更新话题成功.");
    }

    /**
     * 删除话题，如果有相关内容是此话题，则不可以删除.
     *
     * @param topicId
     * @return
     */
    @Override
    public R deleteTopic(String topicId) {
        QueryWrapper<Fish> fishQueryWrapper = new QueryWrapper<>();
        fishQueryWrapper.eq("topic_id", topicId);
        Integer integer = fishService.getBaseMapper().selectCount(fishQueryWrapper);
        if (integer != null && integer > 0) {
            //说明有内容
            return R.FAILED("请先删除该话题下的内容.");
        }
        boolean b = removeById(topicId);
        if (b) {
            return R.SUCCESS("删除话题成功.");
        }
        return R.FAILED("删除话题失败.");
    }

    @Override
    public boolean checkTopic(String topicId) {
        return getById(topicId) != null;
    }

    /**
     * 获取当前用户关注的话题列表
     * 要求登录状态，拿到User
     *
     * @return
     */
    @Override
    public R listFollowTopics() {
        try {
            UserVo user = userRemote.getUser();
            if (user == null) {
                return R.NOT_LOGIN();
            }
            String userId = user.getId();
            List<Topic> topics = this.baseMapper.listMyFollowTopics(userId);
            for (Topic topic : topics) {
                topic.setFollow(true);
            }
            return R.SUCCESS("获取关注话题列表成功.").setData(topics);
        } catch (Exception e) {
            e.printStackTrace();
            return R.SERVER_BUSY();
        }
    }

    @Override
    public R listTopicMenu(int count) {
        //可以不登录，但是我们也要尝试获取一下用户的ID，获取到关注的内容
        UserVo user = null;
        try {
            user = userRemote.getUser();
        } catch (Exception e) {
            return R.SERVER_BUSY();  // 系统繁忙
        }
        List<Topic> resultList;
        if (user == null) {
            //直接获取推荐话题
            resultList = this.baseMapper.listSimpleTopicLimitCount(count);
        } else {
            //获取关注的话题，如果不够就获取非关注的话题进行补充.
            String userId = user.getId();
            resultList = this.baseMapper.listMyFollowTopics(userId);
            if (resultList.size() < count) {
                int dx = count - resultList.size();
                //查询没关注的数据，并且限制个数
                List<Topic> dxTopic = this.baseMapper.listMyUnFollowTopics(userId, dx);
                resultList.addAll(dxTopic);
            }
        }
        return R.SUCCESS("获取话题菜单列表成功.").setData(resultList);
    }

}
