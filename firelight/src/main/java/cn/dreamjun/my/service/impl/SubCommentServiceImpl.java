package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.my.base.MyBaseService;
import cn.dreamjun.my.pojo.Comment;
import cn.dreamjun.my.pojo.Fish;
import cn.dreamjun.my.pojo.SubComment;
import cn.dreamjun.my.mapper.SubCommentMapper;
import cn.dreamjun.my.service.ICommentService;
import cn.dreamjun.my.service.IFishService;
import cn.dreamjun.my.service.ISubCommentService;
import cn.dreamjun.my.vo.SubCommentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
@Service
@Slf4j
public class SubCommentServiceImpl extends MyBaseService<SubCommentMapper, SubComment> implements ISubCommentService {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IFishService fishService;

    /**
     * 添加子评论
     * @param subComment
     * @return
     */
    @Override
    public R addSubComment(SubComment subComment) {
        //检查账号是否有登录
        UserVo user = getUser();
        if (user == null) {
            return R.NOT_LOGIN();
        }
        // 检查数据
        // 数据准确性
        String commentId = subComment.getCommentId();
        if (TextUtils.isEmpty(commentId)) {
            return R.FAILED("评论ID不可以为空");
        }
        // 必须要有的数据（检查评论是否为空）
        String commentContent = subComment.getContent();
        if (TextUtils.isEmpty(commentContent)) {
            return R.FAILED("评论内容不可以为空");
        }
        // 检查回复地评论是否存在
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            return R.FAILED("评论不存在");
        }
        // 检查评论的动态是否存在
        String myId = subComment.getMyId();
        Fish fish = fishService.getById(myId);
        if (fish == null) {
            return R.FAILED("动态不存在");
        }

        commentService.update()
                .setSql("sub_comment_count = sub_comment_count + 1")
                .eq("id", commentId).update();

        // 修改发光动态的评论数量
        fishService.update()
                .setSql("comment_count = comment_count + 1")
                .eq("id", fish.getId()).update();
        // 补充数据
        // 补充该评论回复的子评论 ID 以及回复的用户 UserId
        String subCommentId = subComment.getSubCommentId();
        if (!TextUtils.isEmpty(subCommentId)) {
            // 回复的是子评论
            SubComment replySubComment = getById(subCommentId);
            if (replySubComment == null) {
                return R.FAILED("被评论的内容不存在");
            }
            String targetUserId = replySubComment.getUserId();
            subComment.setTargetUserId(targetUserId);
        } else {
            // 回的是评论
            String targetUserId = comment.getUserId();
            subComment.setTargetUserId(targetUserId);
        }
        // 初始发出改子评论的用户 UserId
        subComment.setUserId(user.getId());
        //保存数据
        save(subComment);
        //发送通知
        //TODO:
        //返回结果
        return R.SUCCESS("感谢您的评论");
    }

    /**
     * 删除子评论
     * @param subCommentId
     * @return
     */
    @Override
    public R removeSubComment(String subCommentId) {
        //检查账号是否有登录
        UserVo user = getUser();
        if (user == null) {
            return R.NOT_LOGIN();
        }
        SubComment subComment = getById(subCommentId);
        if (subComment == null) {
            return R.FAILED("评论内容不存在");
        }
        if (!subComment.getUserId().equals(user.getId())) {
            return R.FAILED("无权限删除");
        }
        boolean result = removeById(subCommentId);
        if (result) {
            return R.SUCCESS("删除成功");
        }
        return R.FAILED("删除失败");
    }

    /**
     * 获取指定发光动态的所有评论
     * @param myId
     * @return
     */
    @Override
    public List<SubCommentVo> listSubCommentsByMyId(String myId) {
        return this.baseMapper.listSubCommentByMyId(myId);
    }
}
