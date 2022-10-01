package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
public interface ICommentService extends IService<Comment> {

    R addComment(Comment comment);

    R removeComment(String id);

    R listComments(int page, String sort, String myId);
}
