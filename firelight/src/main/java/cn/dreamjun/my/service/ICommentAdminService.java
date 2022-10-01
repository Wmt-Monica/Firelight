package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Classname ICommentAdminService
 * @Description TODO
 * @Date 2022/9/22 23:04
 * @Created by 翊
 */
public interface ICommentAdminService extends IService<Comment> {

    R listComment(int page, String myId, String id, String keyword, String userId);

    R removeComment(String commentId);

}
