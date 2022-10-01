package cn.dreamjun.uc.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.uc.service.IUserService;
import cn.dreamjun.uc.vo.LoginVo;
import cn.dreamjun.uc.vo.RegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 翊
 * @since 2022-09-09
 */

/**
 * - 登录 sing_in（post）
 * - 退出登录 sign_out (get)
 * - 注册 sig_up（post）
 */
@Api(tags = "用户管理接口")
@RestController
public class UcPortalUserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "用户注册", notes = "用户-用户注册")
    @PostMapping("/portal/user/register/{emailCode}")
    public R register(@PathVariable("emailCode") String emailCode,
                      @RequestBody RegisterVo registerVo) {
        return userService.addUser(emailCode, registerVo);
    }

    @ApiOperation(value = "用户登录", notes = "用户-用户登录")
    @PostMapping("/portal/user/login")
    public R login(@RequestBody LoginVo loginVo,
                   @RequestParam("verification") String verification) {
        return userService.doLogin(loginVo, verification);
    }

    @ApiOperation(value = "token 解析", notes = "监测用户是否处于登录状态")
    @GetMapping("/portal/user/check/token")
    public R checkToken() {
        return userService.checkToken();
    }

    @ApiOperation(value = "用户退出登录", notes = "用户主动退出登录")
    @GetMapping("/portal/user/logout")
    public R logout() {
        return userService.doLogout();
    }

    @ApiOperation(value = "重置用户信息", notes = "用户更新信息（email / name / password）")
    @PutMapping("/portal/user/user/reset")
    public R resetPassword(@RequestParam("mailCode") String mailCode,
                           @RequestBody RegisterVo registerVo) {
        return userService.resetPassword(mailCode, registerVo);
    }
}

