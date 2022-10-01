package cn.dreamjun.uc.remote;

import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.uc.pojo.UcSettings;
import cn.dreamjun.uc.pojo.UcUser;
import cn.dreamjun.uc.service.ISettingsService;
import cn.dreamjun.uc.service.IUserService;
import cn.dreamjun.uc.service.impl.PermissionCheckServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname UserRemoteImpl
 * @Description TODO
 * @Date 2022/9/15 16:52
 * @Created by 翊
 */
@RestController
public class UserRemoteImpl {

    @Autowired
    private IUserService userService;

    @Autowired
    private PermissionCheckServiceImpl permissionCheckService;

    @Autowired
    private ISettingsService settingsService;

    @GetMapping("/remote/uc/user")
    public UserVo getUser() {
        return userService.getUser();
    }

    @GetMapping("/remote/uc/permission-check")
    public boolean adminPermission() {
        return permissionCheckService.adminPermission();
    }

    @GetMapping("/remote/uc/token")
    UserVo parseToken(@RequestParam("token") String token) {
        return userService.parseToken(token);
    }

}
