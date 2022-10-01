package cn.dreamjun.common.base;

import cn.dreamjun.common.vo.PageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Classname BaseService
 * @Description TODO
 * @Date 2022/9/11 11:05
 * @Created by 翊
 */
public class BaseService<M extends BaseMapper<B>, B> extends ServiceImpl<M, B> {

    protected HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return request;
    }

    protected HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        return response;
    }

    protected int checkPage(int page) {
        if (page < 1) {
            page = 1;
        }
        return page;
    }

    protected <T> PageVo<T> list2Page(List<T> dataList, int page, int size, long totalCount) {
        PageVo<T> pageVo = new PageVo<>();
        pageVo.setList(dataList);
        pageVo.setCurrentPage(page);
        pageVo.setListSize(size);
        //不是第一页，就是有上一页
        pageVo.setHasPre(page != 1);
        pageVo.setTotalCount(totalCount);
        //计算总的页数
        float tempTotalPage = (totalCount * 1.0f / size) + 0.49f;
        int totalPage = Math.round(tempTotalPage);
        pageVo.setTotalPage(totalPage);
        pageVo.setHasNext(page != totalPage);
        return pageVo;
    }

}
