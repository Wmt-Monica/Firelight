package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.base.MyBaseService;
import cn.dreamjun.my.mapper.SubCommentMapper;
import cn.dreamjun.my.pojo.SubComment;
import cn.dreamjun.my.service.ISubCommentAdminService;
import cn.dreamjun.my.vo.SubCommentVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname SubCommentAdminServiceImpl
 * @Description TODO
 * @Date 2022/9/22 23:10
 * @Created by 翊
 */
@Service
public class SubCommentAdminServiceImpl extends MyBaseService<SubCommentMapper, SubComment>
        implements ISubCommentAdminService {

    @Override
    public R listSubComments(String commentId) {
        List<SubCommentVo> subCommentVos = this.baseMapper.listSubCommentByCommentId(commentId);
        return R.SUCCESS("获取子评论列表成功").setData(subCommentVos);
    }

    @Override
    public R removeSubComment(String commentId) {
        boolean result = removeById(commentId);
        if (result) {
            return R.SUCCESS("删除子评论成功");
        }
        return R.FAILED("子评论不存在");
    }
}
