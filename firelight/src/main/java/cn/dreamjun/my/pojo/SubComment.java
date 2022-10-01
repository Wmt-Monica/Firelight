package cn.dreamjun.my.pojo;

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
@TableName("my_sub_comment")
@ApiModel(value="SubComment对象", description="")
public class SubComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "被评论的用户ID")
    private String targetUserId;

    @ApiModelProperty(value = "评论ID")
    private String commentId;

    @ApiModelProperty(value = "子评论ID，回复子评论的时候，需要提交此ID")
    private String subCommentId;

    @ApiModelProperty(value = "摸鱼ID")
    private String myId;

    @ApiModelProperty(value = "点赞数量")
    private Integer thumbUpCount = 0;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
