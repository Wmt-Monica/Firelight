package cn.dreamjun.my.mapper;

import cn.dreamjun.my.pojo.Fish;
import cn.dreamjun.my.vo.FishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
public interface FishMapper extends BaseMapper<Fish> {

    List<FishVo> listFish(long offset, int size, String topicId, String userId);

    long listFishCount(String topicId);

    FishVo getFishById(String fishId);

    List<FishVo> listHotFish(long offset, int count);

    List<FishVo> listRecommendFish(long offset, int size);

    long in30DayCount();

    List<FishVo> listFollowFish(long offset, int size, String userId);

    long listFollowCount(String userId);
}
