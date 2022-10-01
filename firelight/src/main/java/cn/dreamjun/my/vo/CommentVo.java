package cn.dreamjun.my.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Classname CommentVo
 * @Description TODO
 * @Date 2022/9/19 12:57
 * @Created by 翊
 */
@Data
public class CommentVo {
    private String id;
    //用户ID
    private String userId;
    private String avatar;
    private String userName;
    //动态ID
    private String myId;
    // 点赞数量
    private int thumbUpCount;
    //内容
    private String content;
    //创建时间
    private Date createTime;
    //当前用户是否有点赞
    private boolean isLike;
    // 当前评论是否有子评论
    /*
        由于 subCommentList 懒加载的时候不会去查询，因此这里我们不根据 subCommentVoList.size()
        来判断是否存在子评论，直接判断子评论数量 subCommentCount == 1 来进行判断。
     */
    private boolean hasChildren = false;
    // 子评论数量
    private int subCommentCount = 0;
    //子评论列表
    private List<SubCommentVo> subCommentVoList = new ArrayList<>();

    public void setSubCommentCount(int count) {
        subCommentCount = count;
        hasChildren = subCommentCount > 0;
    }
}
