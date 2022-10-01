package cn.dreamjun.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @Classname PageVo
 * @Description TODO
 * @Date 2022/9/11 11:06
 * @Created by 翊
 */
@Data
public class PageVo<T> {
    //数据
    private List<T> list;
    //每一页的数据长度
    private int listSize;
    //当前是第几页
    private int currentPage;
    //总共有多少页
    private int totalPage;
    //总的记录数
    private long totalCount;
    //是否有下一页
    private boolean hasNext;
    //是否有上一页
    private boolean hasPre;


}
