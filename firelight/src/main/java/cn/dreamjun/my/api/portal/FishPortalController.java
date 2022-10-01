package cn.dreamjun.my.api.portal;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.my.pojo.Fish;
import cn.dreamjun.my.service.IFishService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 翊
 * @since 2022-09-14
 */
@RestController
@Api(tags = "【门户】摸鱼接口")
public class FishPortalController {
    @Autowired
    IFishService fishService;

    @PostMapping("/fish")
    @ApiOperation("发表摸鱼")
    public R fish(@RequestBody Fish fish) {
        return fishService.addFish(fish);
    }

    @GetMapping("/fish/list/{page}")
    @ApiOperation("获取摸鱼列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页页码"),
            @ApiImplicitParam(name = "topicId", value = "话题 ID"),
            @ApiImplicitParam(name = "order", value = "排序方式（最新/热门/推荐）"),
            @ApiImplicitParam(name = "userId", value = "用户 ID, 用于用户在查询")
    })
    public R fishList(@PathVariable("page") Integer page,
                      @RequestParam(value = "topicId", required = false) String topicId,
                      @RequestParam(value = "order", required = false) String order,
                      @RequestParam(value = "userId", required = false) String userId) {
        return fishService.listFish(page, topicId, order, userId);
    }

    @ApiImplicitParam(name = "fishId", value = "动态Id")
    @ApiOperation("获取动态详情")
    @GetMapping("/fish/{fishId}")
    public R getFish(@PathVariable("fishId") String fishId) {
        return fishService.getFishById(fishId);
    }

    @ApiImplicitParam(name = "count", value = "期望数量")
    @ApiOperation("获取热门动态")
    @GetMapping("/fish/hot/{count}")
    public R getHotFish(@PathVariable("count") int count) {
        return fishService.listHotFish(count);
    }

}

