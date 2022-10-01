package cn.dreamjun.uc.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.uc.pojo.UcUser;
import cn.dreamjun.uc.vo.LoginVo;
import cn.dreamjun.uc.vo.RegisterVo;
import cn.dreamjun.base.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 翊
 * @since 2022-09-09
 */
public interface IUserService extends IService<UcUser> {

    R addUser(String mailCode, RegisterVo registerVo);

    R doLogin(LoginVo loginVo, String verification);

    R checkToken();

    R doLogout();

    R resetPassword(String mailCode, RegisterVo registerVo);

    R listUser(int page, String phone, String email, String userName, String userId, String status);

    R disableUser(String userId);

    R resetPasswordByUid(String userId, RegisterVo registerVo);

    R initAdminAccount(RegisterVo registerVo);

    UserVo getUser();

    UserVo parseToken(String token);

    R listUserByKeyword(String keyword);
}
