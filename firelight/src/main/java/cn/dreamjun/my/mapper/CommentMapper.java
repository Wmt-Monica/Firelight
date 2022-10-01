package cn.dreamjun.my.mapper;

import cn.dreamjun.my.pojo.Comment;
import cn.dreamjun.my.vo.CommentVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentVo> listComments(int offset, int size, String sort, String myId);

    long listCommentsTotalCount(String myId);

    List<CommentVo> listAdminComments(int size, long offset, String myId, String id, String keyword, String userId);

    long listAdminCommentCount(String myId, String id, String keyword, String userId);
}
