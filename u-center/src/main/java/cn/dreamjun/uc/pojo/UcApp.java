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
@ApiModel(value="UcApp对象", description="")
public class UcApp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "创建者")
    private String userId;

    @ApiModelProperty(value = "1表示可用，0表示不可用")
    private String state;

    @ApiModelProperty(value = "应用的id")
    private String appKey;

    @ApiModelProperty(value = "应用秘钥")
    private String appSecret;

    @ApiModelProperty(value = "回调地址")
    private String callbackUrl;

    @ApiModelProperty(value = "图标地址")
    private String appIcon;

    @ApiModelProperty(value = "应用描述")
    private String appDescription;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
