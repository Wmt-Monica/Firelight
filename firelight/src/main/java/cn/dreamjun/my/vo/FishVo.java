package cn.dreamjun.my.vo;

import cn.dreamjun.my.pojo.Fish;
import lombok.Data;

/**
 * @Classname FishVo
 * @Description TODO
 * @Date 2022/9/18 21:06
 * @Created by 翊
 */
@Data
public class FishVo extends Fish {

    //头像
    private String avatar;
    //用户名
    private String userName;
    //公司
    private String company;
    //职位
    private String position;
    //擅长领域
    private String goodAt;
    //话题名称
    private String topicName;
    //当前用户是否有点赞
    private boolean isLike;
}
