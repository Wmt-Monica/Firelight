package cn.dreamjun.uc.service.impl;

import cn.dreamjun.common.base.BaseService;
import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.common.utils.RedisUtil;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.common.vo.PageVo;
import cn.dreamjun.uc.mapper.UcUserMapper;
import cn.dreamjun.uc.pojo.*;
import cn.dreamjun.uc.service.*;
import cn.dreamjun.uc.utils.ClaimsUtil;
import cn.dreamjun.common.utils.CookieUtils;
import cn.dreamjun.uc.utils.JwtUtil;
import cn.dreamjun.uc.vo.LoginVo;
import cn.dreamjun.uc.vo.RegisterVo;
import cn.dreamjun.uc.vo.UserAdminVo;
import cn.dreamjun.base.vo.UserVo;
import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 翊
 * @since 2022-09-09
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseService<UcUserMapper, UcUser> implements IUserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    IUserInfoService userInfoService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    ITokenService tokenService;

    @Autowired
    ISettingsService settingsService;

    @Autowired
    IRoleService roleService;

    @Autowired
    IUserRoleService userRoleService;


    /**
     * 添加用户
     * @param mailCode 邮箱验证码
     * @param registerVo 注册表视图对象
     * @return
     */
    @Override
    public R addUser(String mailCode, RegisterVo registerVo) {

        String email = registerVo.getEmail();
        String password = registerVo.getPassword();
        String name = registerVo.getName();

        // 判断验证码及其 registerVo 数据是否为空
        if (TextUtils.isEmpty(email)) {
            return R.FAILED("注册邮箱不可以为空");
        }
        if (TextUtils.isEmpty(name)) {
            return R.FAILED("昵称不可以为这空");
        }
        if (TextUtils.isEmpty(password)) {
            return R.FAILED("密码不可以为空");
        }

        // 校验验证码
        // - 1、判断验证码是否为空
        // - 2、判断验证码是否在 Redis 中过期
        String mailCodeRecord = (String) redisUtil.get(Constants.User.KEY_MAIL_CODE + email);
        if (TextUtils.isEmpty(mailCodeRecord) || !mailCodeRecord.equals(mailCode)) {
            return R.FAILED("邮箱验证码不正确");
        }
        // - 3、判断密码是否经过了 md5 的加密
        if (password.length() != 32) {
            return R.FAILED("请使用MD5摘要算法对密码进行转换");
        }
        // - 4、判断邮箱是否已经完成了注册
        QueryWrapper<UcUser> mailQueryWrapper = new QueryWrapper<>();
        mailQueryWrapper.eq("email", email);
        mailQueryWrapper.select("id");
        UcUser ucUser = this.getBaseMapper().selectOne(mailQueryWrapper);
        if (ucUser != null) {
            return R.FAILED("该邮箱已注册");
        }

        // -5、判断用户名称是否与其他用户重名
        QueryWrapper<UcUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_name", name);
        userQueryWrapper.select("id");
        UcUser user = this.baseMapper.selectOne(userQueryWrapper);
        if (user != null) {
            return R.FAILED("该用户名已经注册");
        }

        // 将用户的密码进行加密
        String targetPassword = bCryptPasswordEncoder.encode(password);

        // 根据 RegisterVo 对象转换成 UcUser 对象
        UcUser newUser = new UcUser();
        newUser.setUserName(name);
        newUser.setPassword(targetPassword);
        newUser.setEmail(email);

        // 设置默认的用户头像和状态
        newUser.setAvatar(Constants.User.DEFAULT_AVATAR);
        newUser.setStatus(Constants.User.STATUS_NORMAL);

        // 设置用户的私盐
        newUser.setSalt(IdWorker.getIdStr());

        // 将 UcUser 对象存储到数据库中
        this.baseMapper.insert(newUser);

        // 创建 UcUserInfo 对象
        UcUserInfo newUserInfo = new UcUserInfo();
        newUserInfo.setUserId(newUser.getId());

        // 将 UcUserInfo 对象存储到数据库中
        userInfoService.save(newUserInfo);

        return R.SUCCESS("用户注册成功");
    }

    /**
     * 用户登录
     * @param loginVo 用户登录视图对象
     * @param verification 图灵验证
     * @return
     */
    @Override
    public R doLogin(LoginVo loginVo, String verification) {

        // 校验图灵验证码
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

        // 校验数据
        String name = loginVo.getName();
        if (TextUtils.isEmpty(name)) {
            return R.FAILED("账号不可以为空.");
        }
        String password = loginVo.getPassword();
        if (TextUtils.isEmpty(password)) {
            return R.FAILED("密码不可以为空.");
        }
        if (password.length() != 32) {
            return R.FAILED("请使用MD5摘要算法对密码进行转换.");
        }
        //查询用户
        UcUser userByAccount = this.baseMapper.getUserByAccount(name);
        if (userByAccount == null) {
            return R.FAILED("账号未注册");
        }
        if (Constants.User.STATUS_DISABLE.equals(userByAccount.getStatus())) {
            return R.FAILED("该账号已禁用");
        }
        //对比密码是否正确
        boolean matches = bCryptPasswordEncoder.matches(password, userByAccount.getPassword());
        if (!matches) {
            return R.FAILED("账号或密码不正确");
        }
        //创建token
        createToken(userByAccount);
        UserVo userVo = new UserVo();
        userVo.setAvatar(userByAccount.getAvatar());
        userVo.setUserName(userByAccount.getUserName());
        //返回登录结果
        return R.SUCCESS("登录成功").setData(userVo);
    }

    /**
     * 创建token：
     * - token (有效期2个小时，JWT json web token.放到redis里)
     * - tokenKey (token的 md5摘要值)
     * - refreshToken（有效期1个月，放到数据库里）
     *
     * @param userByAccount
     */
    private UserVo createToken(UcUser userByAccount) {
        // 查询获取当前的 UserVo 对象，其中是携带了 role 标签的用户对象
        UserVo userVo = this.baseMapper.getUserVo(userByAccount.getId());
        // 根据 userByAccount 用户视图对象中的信息封装到 Claims 对象
        Map<String, Object> claims = ClaimsUtil.user2Claim(userVo);
        // 根据 Claims 对象、token 有效时间、用户视图对象的私盐 三个参数来创建 token；存放在 Redis 中
        String token = JwtUtil.createToken(claims, Constants.TimeSecond.TWO_HOUR, userByAccount.getSalt());
        // 根据用户 ID、有效时间、用户视图对象的私盐 三个参数来创建 token；存放在 Mysql 数据库中
        String refreshToken = JwtUtil.createRefreshToken(userByAccount.getId(), Constants.Millions.MONTH, userByAccount.getSalt());
        // 使用 md5 将 token 进行加密，生成 token 的 key 对象
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        // 将 refreshToken 存储到 Redis 中，有效期两小时
        redisUtil.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeSecond.TWO_HOUR);
        // 设置 Cookie 的值
        CookieUtils.setUpCookie(getResponse(), Constants.User.KEY_FIRELIGHT_TOKEN, tokenKey);
        synchronized (userByAccount.getId().intern()) {
            // 将对应存储在 Mysql 中的 Token 数据删除
            QueryWrapper<UcToken> refreshTokenQueryWrapper = new QueryWrapper<>();
            refreshTokenQueryWrapper.eq("user_id", userByAccount.getId());
            tokenService.remove(refreshTokenQueryWrapper);
            // 根据登录信息创建 UcToken 对象，并重新存储在 Mysql 中
            UcToken targetRefreshToken = new UcToken();
            targetRefreshToken.setRefreshToken(refreshToken);
            targetRefreshToken.setTokenKey(tokenKey);
            targetRefreshToken.setUserId(userByAccount.getId());
            tokenService.save(targetRefreshToken);
            // 将 salt 用户登录的 token 私盐保存到 redis 中
            String salt = userByAccount.getSalt();
            redisUtil.set(Constants.User.KEY_SALT + tokenKey, salt, Constants.Millions.TWO_HOUR);
            return userVo;
        }
    }

    /**
     * 解析 token
     * @return
     */
    @Override
    public R checkToken() {
        UserVo userVo = parseUser();
        if (userVo == null) return R.NOT_LOGIN();
        else return R.SUCCESS("已登录", userVo);
    }

    private UserVo parseUser() {
        //先是拿到tokenKey
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.KEY_FIRELIGHT_TOKEN);
        if (TextUtils.isEmpty(tokenKey)) {
            return null;
        }
        return token2UserVo(tokenKey);
    }

    private UserVo token2UserVo(String tokenKey) {
        //先去redis去拿token,有可能没有，有超过2个小时没有活跃了，所以没有了
        String token = (String) redisUtil.get(Constants.User.KEY_TOKEN + tokenKey);
        String salt = (String) redisUtil.get(Constants.User.KEY_SALT + tokenKey);
       // 如果 Redis 中对应用户 salt 为空，那么去 Mysql 中获取盐值，如果 Mysql 中对应的 salt 也不存在在该用户未登录
        if (TextUtils.isEmpty(salt)) {
            return null;
        }

        if (!TextUtils.isEmpty(token)) {
            //有就解析token
            try {
                Claims claims = JwtUtil.parseJWT(token, salt);
                return ClaimsUtil.claim2User(claims);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //走检查refreshToken的路
        return checkRefreshToken(tokenKey, salt);
    }

    /**
     * 从数据库上找到refreshToken,如果没有，就真的没有登录了
     * 如果有，判断是否有过期
     *
     * @param tokenKey
     * @return
     */
    private UserVo checkRefreshToken(String tokenKey, String salt) {
        QueryWrapper<UcToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token_key", tokenKey);
        queryWrapper.select("refresh_token", "user_id");
        UcToken refreshToken = tokenService.getOne(queryWrapper);
        if (refreshToken != null) {
            try {
                // 如果 refreshToken 可以解析，说明RefreshToken没有过期
                JwtUtil.parseJWT(refreshToken.getRefreshToken(), salt);
                // 通过用户ID查到用户的内容，再创建token
                String userId = refreshToken.getUserId();
                // 先删除原来的数据
                redisUtil.del(Constants.User.KEY_TOKEN + tokenKey);
                redisUtil.del(Constants.User.KEY_SALT + tokenKey);
                // 创建新的token
                UcUser user = getById(userId);
                return createToken(user);
            } catch (Exception e) {
                e.printStackTrace();
                //过期了就是走未登录
            }
        }
        return null;
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public R doLogout() {
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.KEY_FIRELIGHT_TOKEN);
        if (TextUtils.isEmpty(tokenKey)) {
            return R.NOT_LOGIN();
        }
        //各种删除
        // token
        redisUtil.del(Constants.User.KEY_TOKEN + tokenKey);
        //refreshToken
        QueryWrapper<UcToken> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token_key", tokenKey);
        tokenService.remove(queryWrapper);
        //salt
        redisUtil.del(Constants.User.KEY_SALT + tokenKey);
        //cookie
        CookieUtils.setUpCookie(getResponse(), Constants.User.KEY_FIRELIGHT_TOKEN, "");
        return R.SUCCESS("退出登录成功.");
    }

    /**
     * 重置用户密码
     * @param mailCode
     * @param registerVo
     * @return
     */
    @Override
    public R resetPassword(String mailCode, RegisterVo registerVo) {
        //检查数据
        String mail = registerVo.getEmail();
        if (TextUtils.isEmpty(mail)) {
            return R.FAILED("邮箱不可以为空");
        }
        String password = registerVo.getPassword();
        if (TextUtils.isEmpty(password)) {
            return R.FAILED("密码不可以为空");
        }
        if (password.length() != 32) {
            return R.FAILED("请使用MD5摘要算法对密码进行转换");
        }
        //先检查邮箱验证码是否正确
        String mailCodeRecord = (String) redisUtil.get(Constants.User.KEY_MAIL_CODE + mail);
        if (mailCodeRecord == null || !mailCodeRecord.equals(mailCode)) {
            return R.FAILED("邮箱验证码不正确");
        }
        //更新数据
        UcUser ucUser = this.baseMapper.getUserByAccount(mail);
        ucUser.setPassword(bCryptPasswordEncoder.encode(password));
        this.baseMapper.updateById(ucUser);
        //退出登录
        doLogout();
        //返回结果
        return R.SUCCESS("密码重置成功.");
    }

    /**
     * 管理员获取用户列表
     * @param page
     * @param phone
     * @param email
     * @param userName
     * @param userId
     * @param status
     * @return
     */
    @Override
    public R listUser(int page, String phone, String email, String userName, String userId, String status) {
        //检查页码，不过小
        page = checkPage(page);
        int size = Constants.DEFAULT_SIZE;
        int offset = (page - 1) * size;
        //查询数据
        if (!TextUtils.isEmpty(userName)) {
            userName = "%" + userName + "%";
        }
        List<UserAdminVo> userAdminVos = this.baseMapper.listUser(size, offset, page, phone, email, userName, userId, status);
        //有总数量
        long totalCount = this.baseMapper.listUserTotalCount(phone, email, userName, userId, status);
        PageVo<UserAdminVo> userAdminVoPageVo = list2Page(userAdminVos, page, size, totalCount);
        return R.SUCCESS("查询用户列表成功").setData(userAdminVoPageVo);
    }

    /**
     * 管理员禁用用户
     * @param userId
     * @return
     */
    @Override
    public R disableUser(String userId) {
        QueryWrapper<UcUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        UcUser ucUser = this.baseMapper.selectOne(queryWrapper);
        if (ucUser == null) {
            return R.FAILED("用户不存在");
        }
        QueryWrapper<UcSettings> adminIdQueryWrapper = new QueryWrapper<>();
        adminIdQueryWrapper.eq("s_key", Constants.User.KEY_ADMIN_USER_ID);
        UcSettings adminSetting = settingsService.getOne(adminIdQueryWrapper);
        if (userId.equals(adminSetting.getSValue())) {
            return R.FAILED("管理员账号不能被禁用");
        }
        ucUser.setStatus(Constants.User.STATUS_DISABLE);
        this.baseMapper.updateById(ucUser);
        //让用户退出登录，不能直接调用logout，这样子会让自己退出登录.
        //可以通过userId去refreshToken的表里查询相关数据，然后退出登录
        doLogoutByUid(userId);
        return R.SUCCESS("已禁用该用户");
    }

    /**
     * 管理员重置用户密码
     * @param userId
     * @param registerVo
     * @return
     */
    @Override
    public R resetPasswordByUid(String userId, RegisterVo registerVo) {
        QueryWrapper<UcUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        UcUser ucUser = this.baseMapper.selectOne(queryWrapper);
        if (ucUser == null) {
            return R.FAILED("用户不存在.");
        }
        if (TextUtils.isEmpty(registerVo.getPassword()) || registerVo.getPassword().length() != 32) {
            return R.FAILED("密码格式不对.");
        }
        String encodePwd = bCryptPasswordEncoder.encode(registerVo.getPassword());
        ucUser.setPassword(encodePwd);
        this.baseMapper.updateById(ucUser);
        //让对应的用户退出登录
        doLogoutByUid(userId);
        return R.SUCCESS("用户密码重置成功.");
    }

    /**
     * 根据用户 ID 指定用户下线
     * @param userId
     */
    private void doLogoutByUid(String userId) {
        //可以通过 userId 去 ucToken 的表里查询相关数据，然后退出登录
        QueryWrapper<UcToken> tokenQueryWrapper = new QueryWrapper<>();
        tokenQueryWrapper.eq("user_id", userId);
        UcToken refreshToken = tokenService.getOne(tokenQueryWrapper);
        // 可能存在该用户已经处于离线的状态，那么就不做处理
        if (refreshToken != null) {
            //各种删除
            // token
            String tokenKey = refreshToken.getTokenKey();
            redisUtil.del(Constants.User.KEY_TOKEN + tokenKey);
            //删除refreshToken
            //salt
            redisUtil.del(Constants.User.KEY_SALT + tokenKey);
            tokenService.remove(tokenQueryWrapper);
        }
    }

    /**
     * 初始化超级管理员对象
     * @param registerVo
     * @return
     */
    @Override
    public R initAdminAccount(RegisterVo registerVo) {
        //先检查是否已经初始化过了
        QueryWrapper<UcSettings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("s_key", Constants.User.KEY_ADMIN_INIT_STATE);
        UcSettings one = settingsService.getOne(queryWrapper);
        if (one != null) {
            return R.FAILED("已经初始化了");
        }
        //没有初始化，检查数据
        if (TextUtils.isEmpty(registerVo.getName())) {
            return R.FAILED("管理员名称不可以为空.");
        }
        if (TextUtils.isEmpty(registerVo.getPassword()) || registerVo.getPassword().length() != 32) {
            return R.FAILED("管理员密码格式错误.");
        }
        // 创建管理员用户对象，完成初始化
        UcUser user = new UcUser();
        user.setPassword(bCryptPasswordEncoder.encode(registerVo.getPassword()));
        user.setUserName(registerVo.getName());
        user.setAvatar(Constants.User.DEFAULT_AVATAR);
        user.setStatus(Constants.User.STATUS_NORMAL);
        user.setSalt(IdWorker.getIdStr());
        user.setEmail(registerVo.getEmail());
        // 将管理员存储到 Mysql 数据库
        save(user);
        // 添加用户信息对象并进行持久化
        UcUserInfo targetUserInfo = new UcUserInfo();
        targetUserInfo.setUserId(user.getId());
        userInfoService.save(targetUserInfo);
        // 以上用户注册完成
        // 添加超级管理员角色
        Role role = new Role();
        role.setLabel(Constants.User.SUPER_ROLE_LABEL);  // 标签
        role.setName(Constants.User.SUPER_ROLE_NAME);  // 角色名称
        roleService.save(role); // 持久化
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        userRoleService.save(userRole);
        // 修改设置的值
        UcSettings settings = new UcSettings();
        settings.setSKey(Constants.User.KEY_ADMIN_INIT_STATE);
        settings.setSValue("1");
        settingsService.save(settings);
        UcSettings adminIdSettings = new UcSettings();
        adminIdSettings.setSKey(Constants.User.KEY_ADMIN_USER_ID);
        adminIdSettings.setSValue(user.getId());
        settingsService.save(adminIdSettings);

        return R.SUCCESS("超级管理员初始化成功");
    }

    @Override
    public UserVo getUser() {
        return parseUser();
    }

    @Override
    public UserVo parseToken(String token) {
        return token2UserVo(token);
    }

    @Override
    public R listUserByKeyword(String keyword) {
        keyword = "%" + keyword + "%";
        List<UcUser> list = new ArrayList<>(this.query()
                .select("user_name", "id")
                .like("user_name", keyword)
                .last("limit 20").list());
        return R.SUCCESS("查询用户列表成功").setData(list);
    }
}
