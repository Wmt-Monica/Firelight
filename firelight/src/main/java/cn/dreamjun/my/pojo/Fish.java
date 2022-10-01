package cn.dreamjun.my.pojo;

import cn.dreamjun.my.config.ImagesTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
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
 * @since 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
// @TableName(autoResultMap = true) 开启映射注解，方便的将数组 / 对象等数据直接映射到实体中
@TableName(value = "my_fish", autoResultMap = true)
@ApiModel(value="Fish对象", description="")
public class Fish implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "话题ID")
    private String topicId;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "链接标题")
    private String urlTitle;

    @ApiModelProperty(value = "链接封面")
    private String urlCover;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "点赞数量")
    private Integer thumbUpCount = 0;

    // 指定转换处理规则
    @TableField(typeHandler = ImagesTypeHandler.class)
    @ApiModelProperty(value = "图片")
    private String[] images;

    @ApiModelProperty(value = "浏览数量")
    private Integer viewCount = 0;

    @ApiModelProperty(value = "评论数量")
    private Integer commentCount = 0;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "逻辑删除标记（0：未删除；1：已删除）")
    private String deleted = "0";

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
