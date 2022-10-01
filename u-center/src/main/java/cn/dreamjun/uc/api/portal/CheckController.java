package cn.dreamjun.uc.api.portal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname CheckController
 * @Description TODO
 * @Date 2022/9/9 9:24
 * @Created by 翊
 */

/**
 * - 获取验证码，verify_code（get）
 * - 获取邮箱验证码，email_code (get)
 * - 检查邮箱是否有注册 email（get）
 * - 检查手机号是否有注册 phone (get)
 * - 检查用户名是否有注册 user_name （get）
 * - 检查token是否有效 token (get)
 */
@RestController
@RequestMapping("/portal/check")
public class CheckController {

}
