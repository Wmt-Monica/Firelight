package cn.dreamjun.uc.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.uc.service.IUserExService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 扩展接口：
 *  发送邮箱接口
 *  检查邮箱是否注册
 *  检查用户名是否注册
 * @Classname UserExController
 * @Description TODO
 * @Date 2022/9/10 10:47
 * @Created by 翊
 */
@Api(tags = "用户扩展接口")
@RestController
@Slf4j
public class UserExController {

    @Autowired
    IUserExService userExService;

    /**
     * 发送注册邮箱验证码
     * @param verification 验证
     * @param mailAddress 邮箱地址
     * @return
     */
    @ApiOperation("发送注册邮箱验证码")
    @GetMapping(value = "/ex/send/register/email-code")
    public R sendRegisterEmailCode(@RequestParam("verification") String verification,
                                   @RequestParam("mail") String mailAddress) {
        log.info("email={}", mailAddress);
        return userExService.sendEmailCode(verification, mailAddress, false);
    }

    /**
     * 发送重置密码邮箱验证码
     * @param verification 验证
     * @param mailAddress 邮箱地址
     * @return
     */
    @ApiOperation("发送重置密码邮箱验证码")
    @GetMapping(value = "/ex/send/reset/email-code")
    public R sendReSetEmailCode(@RequestParam("verification") String verification,
                                @RequestParam("mail") String mailAddress) {
        log.info("email={}", mailAddress);
        return userExService.sendEmailCode(verification, mailAddress, true);
    }

}
