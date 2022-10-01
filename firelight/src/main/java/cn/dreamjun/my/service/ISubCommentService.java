package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.SubComment;
import cn.dreamjun.my.vo.SubCommentVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
public interface ISubCommentService extends IService<SubComment> {

    R addSubComment(SubComment subComment);

    R removeSubComment(String subCommentId);

    List<SubCommentVo> listSubCommentsByMyId(String myId);
}
