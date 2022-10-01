package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.common.utils.RedisUtil;
import cn.dreamjun.my.base.MyBaseService;
import cn.dreamjun.my.pojo.Comment;
import cn.dreamjun.my.pojo.CommentThumbUp;
import cn.dreamjun.my.mapper.CommentThumbUpMapper;
import cn.dreamjun.my.service.ICommentService;
import cn.dreamjun.my.service.ICommentThumbUpService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
public class CommentThumbUpServiceImpl extends MyBaseService<CommentThumbUpMapper, CommentThumbUp> implements ICommentThumbUpService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private ICommentService commentService;

    @Override
    public R doThumbUp(String commentId) {
        // 判断用户是否处于登录状态
        UserVo user = getUser();
        if (user == null) {
            return R.NOT_LOGIN();
        }
        // 数据检查
        // 判断评论是否存在
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            return R.FAILED("评论内容不存在.");
        }
        /*
            避免用户频繁进行点赞 / 取消点赞操作导致点赞数据更新错误，
            使用 synchronized 悲观锁进行数据访问地安全控制
            锁：用户 ID
         */
        synchronized (user.getId().intern()) {
            // 先删除，如果存在说明这次是取消点赞
            QueryWrapper<CommentThumbUp> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user.getId());
            queryWrapper.eq("comment_id", commentId);
            boolean remove = remove(queryWrapper);
            if (remove) {
                // 取消点赞成功
                /*
                    使用 setSql() 传递 SQL 语句的方式交付给 Mysql 进行数据更新
                    数据更新的安全性又 Mysql 数据库中的行锁来得到保证（InnoDB）
                 */
                commentService.update().eq("id", commentId)
                        .setSql("thumb_up_count = thumb_up_count - 1")
                        .update();
                return R.SUCCESS("取消点赞");
            } else {
                // 新加点赞
                // 初始化点赞记录实体对象
                CommentThumbUp commentThumbUp = new CommentThumbUp();
                commentThumbUp.setCommentId(commentId);
                commentThumbUp.setMyId(comment.getMyId());
                commentThumbUp.setUserId(user.getId());
                //添加点赞记录
                save(commentThumbUp);
                //更新点赞数量
                commentService.update().setSql("thumb_up_count = thumb_up_count + 1")
                        .eq("id", commentId).update();
                //返回结果
                return R.SUCCESS("感谢您的点赞");
            }
        }
    }

}
