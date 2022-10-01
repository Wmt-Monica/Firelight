package cn.dreamjun.base.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @Classname UserVo
 * @Description TODO
 * @Date 2022/9/22 14:44
 * @Created by 翊
 */
@Data
@ToString
public class UserVo {
    protected String id;
    protected String sex;
    private String roles;
    protected String status;
    protected String avatar;
    protected String userName;
}
