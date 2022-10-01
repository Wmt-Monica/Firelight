package cn.dreamjun.common.controller;

import cn.dreamjun.base.reponse.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname ErrorPageController
 * @Description TODO
 * @Date 2022/9/27 16:56
 * @Created by 翊
 */
@RestController
public class ErrorPageController {

    @GetMapping("/403")
    public R page403() {
        return R.NO_PERMISSION();
    }
}
