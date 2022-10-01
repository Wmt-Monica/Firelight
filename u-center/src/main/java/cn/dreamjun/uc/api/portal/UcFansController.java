package cn.dreamjun.uc.api.portal;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author 翊
 * @since 2022-09-09
 */

/**
 * - 关注 follow （post）
 * - 取消关注 unfollow (put)
 * - 获取关注列表 follow_list（get）
 * - 获取粉丝列表 fans_list（get）
 * - 加入黑名单 black （post）
 * - 取消黑名单 white (put)
 * - 获取黑名单列表 black_list（get）
 */
@RestController
@RequestMapping("/portal/fans")
public class UcFansController {

}

