package cn.dreamjun.uc.service.impl;

import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.common.utils.CookieUtils;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.uc.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname PermissionCheckServiceImpl
 * @Description TODO
 * @Date 2022/9/27 16:32
 * @Created by 翊
 */
@Service("permission")
public class PermissionCheckServiceImpl {

    @Autowired
    private IUserService userService;

    public boolean adminPermission() {
        // 获取到当前权限所有的角色，进行角色对比即可确定权限
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //如果token返回false
        String token = CookieUtils.getCookie(request, Constants.User.KEY_FIRELIGHT_TOKEN);
        if (TextUtils.isEmpty(token)) {
            return false;
        }
        UserVo user = userService.getUser();
        if (user == null || TextUtils.isEmpty(user.getRoles())) {
            return false;
        }
        return Constants.User.SUPER_ROLE_NAME.contains(user.getRoles());
    }
}
