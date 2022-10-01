package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.common.vo.PageVo;
import cn.dreamjun.my.base.MyBaseService;
import cn.dreamjun.my.mapper.FishAdminMapper;
import cn.dreamjun.my.pojo.Fish;
import cn.dreamjun.my.service.IFishAdminService;
import cn.dreamjun.my.vo.FishVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname FishAdminServiceImpl
 * @Description TODO
 * @Date 2022/9/22 18:02
 * @Created by 翊
 */
@Service
public class FishAdminServiceImpl extends MyBaseService<FishAdminMapper, Fish> implements IFishAdminService {

    @Override
    public R removeFish(String fishId) {
        boolean b = removeById(fishId);
        if (b) {
            return R.SUCCESS("删除动态成功.");
        }
        return R.FAILED("删除动态失败.");
    }

    @Override
    public R listFishes(int page, String topId, String userId, String fishId, String title) {
        page = checkPage(page);
        int size = Constants.DEFAULT_SIZE;
        int offset = (page - 1) * size;
        if (!TextUtils.isEmpty(title)) {
            title = "%" + title + "%";
        }
        List<FishVo> fishVos = this.baseMapper.listFishes(offset, size, topId, userId, fishId, title);
        long count = this.baseMapper.listFishCount(topId, userId, fishId, title);
        PageVo<FishVo> fishVoPageVo = list2Page(fishVos, page, size, count);
        return R.SUCCESS("获取动态列表成功.").setData(fishVoPageVo);
    }

}

