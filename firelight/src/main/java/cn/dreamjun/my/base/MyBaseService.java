package cn.dreamjun.my.base;

import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.common.base.BaseService;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.common.utils.CookieUtils;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.my.remote.UserRemote;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Classname MyBaseService
 * @Description TODO
 * @Date 2022/9/19 12:54
 * @Created by 翊
 */
public class MyBaseService<M extends BaseMapper<B>, B> extends BaseService<M, B> {

    @Autowired
    private UserRemote userRemote;

    protected UserVo getUser() {
        try {
            String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.KEY_FIRELIGHT_TOKEN);
            if (TextUtils.isEmpty(tokenKey)) {
                return null;
            }
            return userRemote.getUser();
        } catch (Exception e) {
            return null;
        }
    }

}

