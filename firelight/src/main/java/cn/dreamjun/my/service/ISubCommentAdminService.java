package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.SubComment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Classname ISubCommentAdminService
 * @Description TODO
 * @Date 2022/9/22 23:05
 * @Created by 翊
 */
public interface ISubCommentAdminService extends IService<SubComment> {

    R listSubComments(String commentId);

    R removeSubComment(String commentId);

}
