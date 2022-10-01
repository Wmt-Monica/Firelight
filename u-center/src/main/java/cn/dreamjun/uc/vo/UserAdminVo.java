package cn.dreamjun.uc.vo;

import cn.dreamjun.base.vo.UserVo;
import lombok.Data;

import java.util.Date;

/**
 * @Classname UserAdminVo
 * @Description TODO
 * @Date 2022/9/12 10:30
 * @Created by 翊
 */
@Data
public class UserAdminVo extends UserVo {
    private String phoneNum;
    private String email;
    private int lev;
    private Date createTime;
    private String company;
    private String position;
    private Date birthday;
    private String goodAt;
    private String location;
}
