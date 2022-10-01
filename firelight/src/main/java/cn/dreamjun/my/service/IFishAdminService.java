package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.Fish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Classname IFishAdminService
 * @Description TODO
 * @Date 2022/9/22 18:00
 * @Created by 翊
 */
public interface IFishAdminService extends IService<Fish> {

    R removeFish(String fishId);

    R listFishes(int page, String topId, String userId, String fishId, String title);
}
