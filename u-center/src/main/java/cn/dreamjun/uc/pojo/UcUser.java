package cn.dreamjun.uc.pojo;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author 翊
 * @since 2022-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UcUser对象", description="")
public class UcUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    private String phoneNum;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "等级")
    private Integer lev = 0;

    @ApiModelProperty(value = "性别")
    private String sex = "0";

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "删除标记")
    @TableField(fill = FieldFill.INSERT)
    private String deleted;

    @ApiModelProperty(value = "用户状态")
    private String status = "0";

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
