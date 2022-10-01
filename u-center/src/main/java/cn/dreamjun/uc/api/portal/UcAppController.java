package cn.dreamjun.uc.api.portal;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author 翊
 * @since 2022-09-09
 */

/**
 * - 创建应用（post）
 * - 修改应用信息（只允许修改logo和名称）(put)
 * - 获取应用列表 list （get)
 * - 获取某个应用的信息（get）
 */
@RestController
@RequestMapping("/portal/app")
public class UcAppController {

}

