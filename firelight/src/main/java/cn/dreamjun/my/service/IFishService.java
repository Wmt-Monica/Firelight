package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.Fish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
public interface IFishService extends IService<Fish> {

    R addFish(Fish fish);

    R listFish(Integer page, String topicId, String order, String userId);

    R getFishById(String fishId);

    R listHotFish(int count);
}
