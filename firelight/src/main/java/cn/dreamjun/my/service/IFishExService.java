package cn.dreamjun.my.service;

import cn.dreamjun.base.reponse.R;

/**
 * @Classname IFishExService
 * @Description TODO
 * @Date 2022/9/16 17:27
 * @Created by 翊
 */
public interface IFishExService {

    R parseUrl(String url);

    R parseHotSearchUrl(String url, String type);
}
