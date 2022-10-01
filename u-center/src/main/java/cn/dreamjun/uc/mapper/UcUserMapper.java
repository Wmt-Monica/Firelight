package cn.dreamjun.uc.mapper;

import cn.dreamjun.uc.pojo.UcUser;
import cn.dreamjun.uc.vo.UserAdminVo;
import cn.dreamjun.base.vo.UserVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 翊
 * @since 2022-09-09
 */
public interface UcUserMapper extends BaseMapper<UcUser> {
    @Select("<script>" +
            "SELECT * FROM `uc_user`" +
            "WHERE `user_name` = #{name} OR `email` = #{name};" +
            "</script>")
    UcUser getUserByAccount(String name);

    @Select("<script>" +
            "SELECT " +
            "uu.`id`,uu.`user_name`,uu.`phone_num`,uu.`email`,uu.`lev`,uu.`sex`,uu.`avatar`,uu.`status`,uu.`create_time`,uui.`company`,uui.`birthday`,uui.`position`,uui.`good_at`,uui.`location`" +
            "FROM `uc_user` uu " +
            "LEFT JOIN `uc_user_info` uui ON uu.`id` = uui.`user_id` " +
            "<where> 1=1 " +
            "<if test=\"email!=null and email!=''\"> and uu.`email` = #{email} </if>" +
            "<if test=\"phone!=null and phone!=''\"> and uu.`phone_num` = #{phone} </if> " +
            "<if test=\"userName!=null and userName!=''\"> and uu.`user_name` like #{userName} </if> " +
            "<if test=\"userId!=null and userId!=''\"> and uu.`id` = #{userId} " +
            "</if> <if test=\"status!=null and status!=''\"> and uu.`status` = #{status} " +
            "</if> " +
            "</where> " +
            "ORDER BY uu.`create_time` DESC LIMIT #{offset},#{size}" +
            "</script>")
    List<UserAdminVo> listUser(int size, int offset, int page, String phone, String email, String userName, String userId, String status);

    @Select("<script>" +
            "SELECT count(uu.`id`) " +
            "FROM `uc_user` uu " +
            "<where> 1=1 " +
            "<if test=\"email!=null and email!=''\"> and uu.`email` = #{email} </if> " +
            "<if test=\"phone!=null and phone!=''\"> and uu.`phone_num` = #{phone} " +
            "</if> <if test=\"userName!=null and userName!=''\"> and uu.`user_name` like #{userName} </if> " +
            "<if test=\"userId!=null and userId!=''\"> and uu.`id` = #{userId} </if> " +
            "<if test=\"status!=null and status!=''\"> and uu.`status` = #{status} </if> " +
            "</where>" +
            "</script>")
    long listUserTotalCount(String phone, String email, String userName, String userId, String status);

    @Select("<script>" +
            "SELECT GROUP_CONCAT(ur.`name`) AS roles,uu.`id`,uu.`sex`,uu.`status`,uu.`avatar`,uu.`user_name` " +
            "FROM `uc_user` uu " +
            "LEFT JOIN `uc_user_role` uur ON uu.`id` = uur.`user_id` " +
            "LEFT JOIN `uc_role` ur ON uur.`role_id` = ur.`id`  " +
            "WHERE uu.`id` = #{userId}" +
            "</script>")
    UserVo getUserVo(String userId);
}
