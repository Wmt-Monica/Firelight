package cn.dreamjun.my.mapper;

import cn.dreamjun.my.pojo.Fish;
import cn.dreamjun.my.vo.FishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Classname FishAdminMapper
 * @Description TODO
 * @Date 2022/9/22 18:03
 * @Created by 翊
 */
public interface FishAdminMapper extends BaseMapper<Fish> {

    List<FishVo> listFishes(int offset, int size, String topicId,
                            String userId, String fishId, String title);

    long listFishCount(String topicId, String userId, String fishId, String title);
}
