package cn.dreamjun.my.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Classname TopicVo
 * @Description TODO
 * @Date 2022/9/22 20:38
 * @Created by 翊
 */
@Data
@TableName("my_topic")
public class TopicVo {

    // 话题 Id
    private String id;

    // 话题名称
    @TableField("`name`")
    private String name;
}
