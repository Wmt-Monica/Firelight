package cn.dreamjun.uc.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.base.BaseService;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.common.utils.RedisUtil;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.uc.mapper.UcUserMapper;
import cn.dreamjun.uc.pojo.UcUser;
import cn.dreamjun.uc.service.IUserService;
import cn.dreamjun.uc.service.IUserExService;
import cn.dreamjun.uc.utils.EmailSender;
import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname UserExServiceImpl
 * @Description TODO
 * @Date 2022/9/10 10:56
 * @Created by 翊
 */
@Service
@Slf4j
@EnableAsync
public class UserExServiceImpl extends BaseService<UcUserMapper, UcUser> implements IUserExService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    IUserService userService;

    @Autowired
    private CaptchaService captchaService;

    /**
     * 发送验证码
     * @param verification 验证
     * @param emailAddress 邮箱地址
     * @param mustRegister 邮箱是否必须已注册
     * @return
     */
    @Async("sendEmail")
    @Override
    public R sendEmailCode(String verification, String emailAddress, Boolean mustRegister) {
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(verification);

        //这种验证方式是会删除值，只能验证一次。
        ResponseModel response = captchaService.verification(captchaVO);
        String repCode = response.getRepCode();
        if (!response.isSuccess()) {
            //验证码校验失败，返回信息告诉前端
            //repCode  0000  无异常，代表成功
            //repCode  9999  服务器内部异常
            //repCode  0011  参数不能为空
            //repCode  6110  验证码已失效，请重新获取
            //repCode  6111  验证失败
            //repCode  6112  获取验证码失败,请联系管理员
            if (repCode != null && repCode.equals(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getCode())) {
                return R.FAILED(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR.getDesc());
            } else if (repCode == null || !repCode.equals(RepCodeEnum.SUCCESS.getCode())) {
                return R.FAILED("图灵码验证失败.");
            }
        }
        //先检查数据
        if (TextUtils.isEmpty(emailAddress)) {
            return R.FAILED("邮箱地址不可以为空.");
        }
        // 验证邮箱的格式
        String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(emailAddress);
        if (!m.matches()) {
            return R.FAILED("邮箱格式错误");
        }
        // 检查验证码是否必须注册
        QueryWrapper<UcUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", emailAddress);
        UcUser ucUser = userService.getBaseMapper().selectOne(userQueryWrapper);
        if (ucUser == null && mustRegister) {
            return R.FAILED("该邮箱未注册");
        }
        if (ucUser != null && !mustRegister) {
            return R.FAILED("该邮箱已注册");
        }

        // 防止恶意的调用
        // 获取请求 IP 地址
        String remoteAddr = getRequest().getRemoteAddr();
        // 将 192.168.94.125 IP 中的 '.' 替换成 '-'
        remoteAddr = remoteAddr.replaceAll(":", "-");
        // 先判断是否有多次请求，2个小时内，超过10次，就返回频繁请求的警告
        String ipKey = Constants.User.KEY_MAIL_CODE_IP + remoteAddr;
        String ipTime = (String) redisUtil.get(ipKey);
        if (!TextUtils.isEmpty(ipTime)) {
            int i = Integer.parseInt(ipTime);
            if (i > Constants.EMAIL_CODE_SEND_RETRY_MAX_TIMES) {
                return R.FAILED("调用过于频繁");
            } else {
                i++;
                redisUtil.set(ipKey, String.valueOf(i), Constants.TimeSecond.TWO_HOUR);
            }
        } else {
            // 两小时内有效果
            redisUtil.set(ipKey, "1", Constants.TimeSecond.TWO_HOUR);
        }

        // 邮箱不允许频繁发送
        String addressKey = Constants.User.KEY_MAIL_CODE_ADDRESS + emailAddress;
        String addressTime = (String) redisUtil.get(addressKey);
        if (!TextUtils.isEmpty(addressTime)) {
            int i = Integer.parseInt(addressTime);
            // 如果邮箱重复次数太高则停止重发
            if (i > Constants.EMAIL_CODE_SEND_RETRY_MAX_TIMES) {
                return R.FAILED("调用过于频繁");
            } else {
                i++;
                redisUtil.set(addressKey, String.valueOf(i), Constants.TimeSecond.TWO_HOUR);
            }
        } else {
            // 两小时内有效果
            redisUtil.set(addressKey, "1", Constants.TimeSecond.TWO_HOUR);
        }

        // 产生 6 位数的验证码
        Random random = new Random();
        int mailCode = random.nextInt(999999);
        if (mailCode < 100000) {
            mailCode += 100000;
        }
        log.info("mail code ==> " + mailCode);
        // redis 记录验证码
        redisUtil.set(Constants.User.KEY_MAIL_CODE + emailAddress,
                String.valueOf(mailCode), Constants.TimeSecond.FIVE_MIN);

        // 发送验证码
        EmailSender.sendMailCode(emailAddress, "验证码：" + mailCode + ",5分钟内有效");
        return R.SUCCESS("邮箱验证码发送成功");
    }
}
