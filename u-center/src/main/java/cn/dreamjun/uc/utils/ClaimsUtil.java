package cn.dreamjun.uc.utils;

import cn.dreamjun.base.vo.UserVo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname ClaimsUtil
 * @Description TODO
 * @Date 2022/9/11 9:55
 * @Created by 翊
 */
public class ClaimsUtil {

    public static final String ID = "id";
    public static final String SEX = "sex";
    public static final String ROLES = "roles";
    public static final String STATUS = "status";
    public static final String AVATAR = "avatar";
    public static final String USERNAME = "userName";

    public static Map<String, Object> user2Claim(UserVo userByAccount) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID, userByAccount.getId());
        claims.put(SEX, userByAccount.getSex());
        claims.put(ROLES, userByAccount.getRoles());
        claims.put(STATUS, userByAccount.getStatus());
        claims.put(AVATAR, userByAccount.getAvatar());
        claims.put(USERNAME, userByAccount.getUserName());
        return claims;
    }


    public static UserVo claim2User(Map<String, Object> claims) {
        UserVo userVo = new UserVo();
        String id = (String) claims.get(ID);
        userVo.setId(id);
        String sex = (String) claims.get(SEX);
        userVo.setSex(sex);
        String roles = (String) claims.get(ROLES);
        userVo.setRoles(roles);
        String status = (String) claims.get(STATUS);
        userVo.setStatus(status);
        String avatar = (String) claims.get(AVATAR);
        userVo.setAvatar(avatar);
        String userName = (String) claims.get(USERNAME);
        userVo.setUserName(userName);
        return userVo;
    }
}
