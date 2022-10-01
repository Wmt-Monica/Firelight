package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.common.vo.PageVo;
import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.my.base.MyBaseService;
import cn.dreamjun.my.pojo.Fish;
import cn.dreamjun.my.mapper.FishMapper;
import cn.dreamjun.my.pojo.ThumbUp;
import cn.dreamjun.my.pojo.Topic;
import cn.dreamjun.my.service.IFishService;
import cn.dreamjun.my.service.IThumbUpService;
import cn.dreamjun.my.service.ITopicService;
import cn.dreamjun.my.vo.FishVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class FishServiceImpl extends MyBaseService<FishMapper, Fish> implements IFishService {

    @Autowired
    ITopicService topicService;

    @Autowired
    IThumbUpService thumbUpService;

    /**
     * 发表摸鱼
     * @param fish
     * @return
     */
    @Override
    public R addFish(Fish fish) {
        //确保登录的状态
        UserVo user = getUser();
        if (user == null) {
            return R.NOT_LOGIN();
        }
        //已经登录的状态
        //检查数据
        String topicId = fish.getTopicId();
        if (!TextUtils.isEmpty(topicId)) {
            //如果有话题ID，要检查话题ID是否合法
            QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", fish.getTopicId());
            queryWrapper.select("id");
            int count = topicService.count(queryWrapper);
            if (count == 0) {
                //此话题ID不存在.
                return R.FAILED("此话题ID不存在");
            }
        }

        //检查内容
        String content = fish.getContent();
        if (TextUtils.isEmpty(content)) {
            return R.FAILED("内容不可以为空");
        }
        String[] images = fish.getImages();
        if (images != null && images.length > 9) {
            return R.FAILED("图片最多支持9张");
        }
        //补充内容
        fish.setUserId(user.getId());
        save(fish);
        return R.SUCCESS("发光成功");
    }

    /**
     * 根据不同的排序规则及其页码返回摸鱼动态列表
     * @return
     */
    @Override
    public R listFish(Integer page, String topicId, String order, String userId) {
        //page进行检查
        page = checkPage(page);
        int size = Constants.DEFAULT_SIZE;
        long offset = size * (page - 1);
        List<FishVo> fishVos;
        long totalCount;
        // topicId
        //当topicId为：new,hot,follow-->最新，热门，关注
        if ("new".equals(topicId)) {
            //加载最新，按时间排序
            topicId = "";
        }
         if ("hot".equals(topicId)) {
            // 评论多的，30天内
            fishVos = this.baseMapper.listHotFish(offset, size);
            totalCount = this.baseMapper.in30DayCount();
        } else if ("recommend".equals(topicId)) {
            // 按点赞数量, 30天内
            fishVos = this.baseMapper.listRecommendFish(offset, size);
            totalCount = this.baseMapper.in30DayCount();
        } else if ("follow".equals(topicId)) {
            // 关注的
            fishVos = this.baseMapper.listFollowFish(offset, size, userId);
            totalCount = this.baseMapper.listFollowCount(userId);
        } else {
            fishVos = this.baseMapper.listFish(offset, size, topicId, userId);
            totalCount = this.baseMapper.listFishCount(topicId);
        }
        //处理fishVos当前用户的点赞状态
        handleThumbUp(fishVos);
        PageVo<FishVo> fishVoPageVo = list2Page(fishVos, page, size, totalCount);
        return R.SUCCESS("获取动态列表成功").setData(fishVoPageVo);
    }

    @Override
    public R getFishById(String fishId) {
        // 检查发光动态是否存在
        FishVo fish = this.baseMapper.getFishById(fishId);
        if (fish == null) {
            return R.FAILED("动态不存在");
        }
        //处理点赞状态
        //如果没有登录，不用处理
        UserVo user = getUser();
        if (user != null) {
            Integer count = thumbUpService.query().eq("user_id", user.getId())
                    .eq("my_id", fish.getId()).count();
            fish.setLike(count != null && count > 0);
        }
        //如果有登录了，查询当前动态ID+用户ID的点赞是否存在.
        return R.SUCCESS("获取动态内容成功").setData(fish);
    }

    @Override
    public R listHotFish(int count) {
        if (count > Constants.DEFAULT_HOST_SIZE) {
            count = Constants.DEFAULT_HOST_SIZE;
        }
        List<FishVo> fishVos = this.baseMapper.listHotFish(0, count);
        return R.SUCCESS("获取热门动态成功").setData(fishVos);
    }

    /**
     * 处理当前用户的点赞状态.
     *
     * @param fishVos
     */
    private void handleThumbUp(List<FishVo> fishVos) {
        if (fishVos == null) {
            return;
        }
        //判断是否有登录
        UserVo user = getUser();
        if (user != null) {
            // 有登录，就要处理点赞状态
            // 查询出当前用户的点赞列表
            List<ThumbUp> thumbUps = thumbUpService.query().eq("user_id", user.getId()).list();
            // 转成map，减少循环次数，直接通过key去拿值
            Map<String, ThumbUp> thumbUpMap = thumbUps.stream().collect(Collectors.toMap(ThumbUp::getMyId, item -> item));
            for (FishVo fishVo : fishVos) {
                String id = fishVo.getId();
                ThumbUp thumbUp = thumbUpMap.get(id);
                fishVo.setLike(thumbUp != null);
            }
        }
    }
}
