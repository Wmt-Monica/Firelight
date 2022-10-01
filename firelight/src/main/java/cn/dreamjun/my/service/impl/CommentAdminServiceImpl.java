package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.common.vo.PageVo;
import cn.dreamjun.my.base.MyBaseService;
import cn.dreamjun.my.mapper.CommentMapper;
import cn.dreamjun.my.pojo.Comment;
import cn.dreamjun.my.pojo.SubComment;
import cn.dreamjun.my.service.ICommentAdminService;
import cn.dreamjun.my.service.ISubCommentAdminService;
import cn.dreamjun.my.vo.CommentVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname CommentAdminServiceImpl
 * @Description TODO
 * @Date 2022/9/22 23:07
 * @Created by 翊
 */
@Service
public class CommentAdminServiceImpl extends MyBaseService<CommentMapper, Comment> implements ICommentAdminService {

    @Autowired
    private ISubCommentAdminService subCommentAdminService;

    /**
     * 获取评论列表
     *
     * @param page    页码，从1开始
     * @param myId    动态ID
     * @param id      评论ID
     * @param keyword 关键字
     * @param userId  用户ID
     * @return
     */
    @Override
    public R listComment(int page, String myId, String id, String keyword, String userId) {
        page = checkPage(page);
        //处理分页
        int size = Constants.DEFAULT_SIZE;
        long offset = (page - 1) * size;
        //关键字处理
        if (!TextUtils.isEmpty(keyword)) {
            keyword = "%" + keyword + "%";
        }
        List<CommentVo> commentVos = this.baseMapper.listAdminComments(size, offset, myId, id, keyword, userId);
        long totalCount = this.baseMapper.listAdminCommentCount(myId, id, keyword, userId);
        PageVo<CommentVo> commentVoPageVo = list2Page(commentVos, page, size, totalCount);
        return R.SUCCESS("获取评论列表成功").setData(commentVoPageVo);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论的ID
     * @return
     */
    @Override
    public R removeComment(String commentId) {
        //先删除这个评论下的子评论
        QueryWrapper<SubComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId);
        subCommentAdminService.remove(queryWrapper);
        //再删除评论
        removeById(commentId);
        return R.SUCCESS("删除评论成功");
    }
}
