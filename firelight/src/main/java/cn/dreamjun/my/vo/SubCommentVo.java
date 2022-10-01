package cn.dreamjun.my.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Classname SubCommentVo
 * @Description TODO
 * @Date 2022/9/19 12:57
 * @Created by 翊
 */
@Data
public class SubCommentVo {
    private String id;
    //自己的用户信息
    private String userId;
    private String avatar;
    private String userName;
    //职位
    private String position;
    //公司
    private String company;

    //被回复用户的信息
    private String targetUserId;
    private String targetAvatar;
    private String targetUserName;
    //评论内容
    private String commentId;
    private String myId;
    private String thumbUpCount;
    private String content;
    private Date createTime;
}
