package cn.dreamjun.my.mapper;

import cn.dreamjun.my.pojo.SubComment;
import cn.dreamjun.my.vo.SubCommentVo;
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
public interface SubCommentMapper extends BaseMapper<SubComment> {

    List<SubCommentVo> listSubCommentByMyId(String myId);

    List<SubCommentVo> listSubCommentByCommentId(String commentId);
}
