package cn.dreamjun.uc.vo;

import lombok.Data;

/**
 * @Classname RegisterVo
 * @Description TODO
 * @Date 2022/9/10 14:07
 * @Created by 翊
 */
@Data
public class RegisterVo {

    //用户ID
    private String id;
    //邮箱
    private String email;
    //昵称
    private String name;
    //密码
    private String password;

}
