package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.my.base.MyBaseService;
import cn.dreamjun.my.pojo.Fish;
import cn.dreamjun.my.pojo.ThumbUp;
import cn.dreamjun.my.mapper.ThumbUpMapper;
import cn.dreamjun.my.service.IFishService;
import cn.dreamjun.my.service.IThumbUpService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
@Service
public class ThumbUpServiceImpl extends MyBaseService<ThumbUpMapper, ThumbUp> implements IThumbUpService {

    @Autowired
    private IFishService fishService;

    @Override
    public R thumbUp(String fishId) {
        // 判断用户的登录状态
        try {
            UserVo user = getUser();
            if (user == null) {
                return R.NOT_LOGIN();
            }
            //数据检查
            //判断动态是否存在
            Fish fish = fishService.getById(fishId);
            if (fish == null) {
                return R.FAILED("动态不存在");
            }
            /*
                避免同一个用户频繁进行点赞 / 取消点赞操作可能带来的数据更新错误
                使用悲观锁保证数据地安全访问
             */
            synchronized (user.getId().intern()) {
                /*
                    首先根据动态 ID (my_id) 和用户 ID (user_id) 去数据库先删除数据
                    - remove (判断删除操作的标识)
                        - true，表明该用户给该动态存在点赞记录（取消点赞操作）
                        - false，表明还用户没有给该动态点赞的记录（点赞操作）
                    通过判断 Mysql 中是否存在点赞记录来判断是 点赞 / 取消点赞的操作
                 */
                QueryWrapper<ThumbUp> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", user.getId());
                queryWrapper.eq("my_id", fishId);
                boolean remove = remove(queryWrapper);
                if (remove) {
                    // 取消点赞操作
                    // 修改动态的点赞数 - 1，并写回数据库中去
                    // 使用 setSql() 设置语句，利用 Mysql 数据表中的行锁（InnoDB）来确保数据修改的安全性
                    fishService.update()
                            .setSql("thumb_up_count=thumb_up_count-1")
                            .eq("id", fishId)
                            .update();
                    //返回取消点赞成功
                    return R.SUCCESS("取消点赞");
                } else {
                    // 点赞操作
                    // 封装 thumbUp 对象，将该记录写回点赞表 my_thumb_up 中去
                    ThumbUp thumbUp = new ThumbUp(fishId, user.getId());
                    boolean save = save(thumbUp);
                    // 如果成功插入点赞记录表
                    if (save) {
                        //更新点赞数量
                        // 修改动态的点赞数 + 1，并写回数据库中去
                        fishService.update()
                                .setSql("thumb_up_count=thumb_up_count+1")
                                .eq("id", fishId)
                                .update();
                        //返回点赞成功.
                        return R.SUCCESS("感谢您的点赞");
                    }
                }
            }
            //返回结果
            return R.FAILED("点赞失败");
        } catch (Exception e) {
            e.printStackTrace();
            return R.SERVER_BUSY();
        }
    }
}
