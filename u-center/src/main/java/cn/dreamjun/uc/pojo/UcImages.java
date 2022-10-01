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
@ApiModel(value="UcImages对象", description="")
public class UcImages implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "MD5值")
    private String md5;

    @ApiModelProperty(value = "文件名称")
    private String name;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "类型：avatar,app_icon,cover")
    private String type;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
