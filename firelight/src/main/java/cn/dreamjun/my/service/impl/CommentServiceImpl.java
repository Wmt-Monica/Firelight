package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.common.utils.TextUtils;
import cn.dreamjun.common.vo.PageVo;
import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.my.base.MyBaseService;
import cn.dreamjun.my.pojo.Comment;
import cn.dreamjun.my.mapper.CommentMapper;
import cn.dreamjun.my.pojo.CommentThumbUp;
import cn.dreamjun.my.pojo.Fish;
import cn.dreamjun.my.pojo.SubComment;
import cn.dreamjun.my.service.ICommentService;
import cn.dreamjun.my.service.ICommentThumbUpService;
import cn.dreamjun.my.service.IFishService;
import cn.dreamjun.my.service.ISubCommentService;
import cn.dreamjun.my.vo.CommentVo;
import cn.dreamjun.my.vo.SubCommentVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
@Service
public class CommentServiceImpl extends MyBaseService<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private IFishService fishService;

    @Autowired
    private ISubCommentService subCommentService;

    @Autowired
    private ICommentThumbUpService commentThumbUpService;

    /**
     * 发表评论
     * @param comment
     * @return
     */
    @Override
    public R addComment(Comment comment) {
        // 检查用户是否有登录
        UserVo user = getUser();
        if (user == null) {
            return R.NOT_LOGIN();
        }
        // 检查数据
        // 动态是否是存在的
        String myId = comment.getMyId();
        Fish fish = fishService.getById(myId);
        if (fish == null) {
            return R.FAILED("发光动态不存在");
        }
        // 检查评论内容是否为空
        String commentContent = comment.getContent();
        if (TextUtils.isEmpty(commentContent)) {
            return R.FAILED("评论内容不可以为空");
        }
        // 修改发光动态的评论数量
        fishService.update()
                .setSql("comment_count = comment_count + 1")
                .eq("id", fish.getId()).update();
        // 补充数据（评论的用户 ID）
        comment.setUserId(user.getId());
        // 数据入库
        save(comment);
        // 返回结果
        //if (!fish.getUserId().equals(user.getId())) {
        //TODO:发通知（向该条动态的发布者发送其他用户评论通知）
        //}
        return R.SUCCESS("评论成功");
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @Override
    public R removeComment(String id) {
        UserVo user = getUser();
        if (user == null) {
            return R.NOT_LOGIN();
        }
        //校验身份，只能删除自己的评论
        Comment comment = getById(id);
        if (comment == null) {
            return R.FAILED("评论不存在");
        }
        String userId = comment.getUserId();
        if (!user.getId().equals(userId)) {
            return R.FAILED("无权限删除该评论");
        }
        //删除子评论
        QueryWrapper<SubComment> subCommentQueryWrapper = new QueryWrapper<>();
        subCommentQueryWrapper.eq("comment_id", id);
        subCommentService.remove(subCommentQueryWrapper);
        //删除评论
        removeById(id);
        return R.SUCCESS("删除评论成功");
    }

    /**
     * 获取评论列表
     * @param page
     * @param sort
     * @param myId
     * @return
     */
    @Override
    public R listComments(int page, String sort, String myId) {
        //不要求登录状态
        //页码要从1开始
        page = checkPage(page);
        int size = Constants.DEFAULT_SIZE;
        int offset = (page - 1) * size;
        //检查排序类型
        if (TextUtils.isEmpty(sort) || (!Comment.SORT_DEFAULT.equals(sort) &&
                !Comment.SORT_THUMB.equals(sort))) {
            sort = Comment.SORT_DEFAULT;
        }
        // 判断用户是否有登录，如果未登录，不需要处理点赞状态
        UserVo user = getUser();
        Map<String, String> commentThumbMap = null;
        if (user != null) {
            // 获取该用户的评论点赞列表（优先展示当前登录用户该动态的评论及其点赞信息）
            List<CommentThumbUp> commentThumbUpList =
                    commentThumbUpService.query()
                            .select("comment_id").eq("user_id", user.getId()).list();
            //转成map去处理
            commentThumbMap = commentThumbUpList.stream().collect(Collectors.toMap(CommentThumbUp::getCommentId, CommentThumbUp::getCommentId));
        }
        // （分页）查询获取评论列表
        List<CommentVo> commentVos = this.baseMapper.listComments(offset, size, sort, myId);
        // 如果点赞表不为 null，那么就处理评论表中的 like 属性的赋值
        if (commentThumbMap != null) {
            for (CommentVo commentVo : commentVos) {
                //设置是否有点赞
                String id = commentVo.getId();
                commentVo.setLike(commentThumbMap.get(id) != null);
            }
        }
        // 将评论视图 CommentVo 对象转成Map集集合，减少查询时遍历次数
        Map<String, CommentVo> commentVoMap = commentVos.stream().collect(Collectors.toMap(CommentVo::getId, item -> item));
        // 查询当前动态下的所有子评论，查出来再分到每个评论下
        // 原来的遍历次数是相乘，现在是相加。当1000评论+1000子评论的时候，2000，1000000（减少查询的次数）
        List<SubCommentVo> subCommentVos = subCommentService.listSubCommentsByMyId(myId);
        if (subCommentVos != null) {
            // 如果该动态的子评论不为空，那么采用 O(n) 的方式遍历子评论集合
            for (SubCommentVo subCommentVo : subCommentVos) {
                // 根据遍历的所有子评论的 commentID 评论 ID 从评论 Map 集合中获取设置 like 属性
                String commentId = subCommentVo.getCommentId();
                CommentVo commentVo = commentVoMap.get(commentId);
                if (commentVo != null) {
                    //把子评论添加到评论列表里去
                    commentVo.getSubCommentVoList().add(subCommentVo);
                }
            }
        }
        long totalCount = this.baseMapper.listCommentsTotalCount(myId);
        PageVo<CommentVo> commentVoPageVo = list2Page(commentVos, page, size, totalCount);
        return R.SUCCESS("获取评论列表成功.").setData(commentVoPageVo);
    }
}
