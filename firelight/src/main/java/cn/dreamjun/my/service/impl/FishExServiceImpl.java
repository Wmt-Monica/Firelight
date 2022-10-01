package cn.dreamjun.my.service.impl;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.common.utils.Constants;
import cn.dreamjun.my.service.IFishExService;
import cn.dreamjun.my.vo.UrlParseVo;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

/**
 * @Classname FishExServiceImpl
 * @Description TODO
 * @Date 2022/9/16 17:34parseHotSearchUrl
 * @Created by 翊
 */
@Slf4j
@Service
public class FishExServiceImpl implements IFishExService {

    /**
     * 解析 URL
     * @param url
     * @return
     */
    @Override
    public R parseUrl(String url) {
        try {
            // 解析 URL，返回 Document 文档对象（并设置解析 URL 的最大超时时间，）
            Document doc = Jsoup.connect(url).timeout(1000 * 60).get();
            // 获取 doc 文件标题
            String title = doc.title();
            // 获取 doc 所有 img 图片元素
            Elements imageElements = doc.select("img");
            String src = null;
            if (imageElements.size() > 0) {
                // 随机获取图片元素集合中的某个图片元素对象
                Element element = imageElements.get(new Random().nextInt(imageElements.size()));
                // 获取图片元素对象的 src 属性
                src = element.attr("src");
            }
            // 封装 URL 解析视图对象
            UrlParseVo urlParseVo = new UrlParseVo();
            urlParseVo.setUrl(url);
            urlParseVo.setTitle(title);
            urlParseVo.setCover(src);
            return R.SUCCESS("链接解析成功").setData(urlParseVo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.FAILED("链接解析失败");
    }

    @Override
    public R parseHotSearchUrl(String url, String type) {
        try {
            // 解析 URL，返回 Document 文档对象（并设置解析 URL 的最大超时时间，）
            String body = Jsoup
                    .connect(url)
                    .timeout(1000 * 100)
                    .ignoreContentType(true)
                    .execute()
                    .body();
            body = body.replaceAll("\\n", "");
            String substring = "";
            if (type.equals(Constants.URLType.ZHI_HU)) {
                substring = body.substring(body.indexOf("\"data\":") + 7, body.indexOf(",\"paging"));
            } else if (type.equals(Constants.URLType.BI_LI_BI_LI)) {
                substring = body.substring(body.indexOf("\"list\":") + 7, body.indexOf(",\"no_more\":"));
            } else {
                return R.FAILED("不支持解析");
            }
            System.out.println(substring);
            JSONArray objects = JSONArray.parseArray(substring);
            return R.SUCCESS("解析成功", objects);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.FAILED("解析失败");
    }

}
