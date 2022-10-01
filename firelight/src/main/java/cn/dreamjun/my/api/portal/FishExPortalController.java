package cn.dreamjun.my.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.service.IFishExService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname FishExPortalController
 * @Description TODO
 * @Date 2022/9/16 17:26
 * @Created by 翊
 */
@Api(tags = "【门户】摸鱼扩展接口")
@RestController
public class FishExPortalController {
    @Autowired
    private IFishExService fishExService;

    @ApiOperation("解析URL")
    @ApiImplicitParam(name = "url", value = "要解析的链接")
    @GetMapping("/fish/ex/parse-url")
    public R parseUrl(@RequestParam("url") String url) {
        return fishExService.parseUrl(url);
    }

    @ApiOperation("解析热门网站热搜URL")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "要解析的链接"),
            @ApiImplicitParam(name = "type", value = "要解析的链接网站类型（zhihu / weibo / bilibili）")
    })
    @GetMapping("/fish/ex/parse-hot-search-url")
    public R parseHotSearchUrl(@RequestParam("url") String url, @RequestParam("type") String type) {
        return fishExService.parseHotSearchUrl(url, type);
    }
}
