package cn.dreamjun.my.remote;

import cn.dreamjun.base.config.FeignConfiguration;
import cn.dreamjun.base.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Classname UserRemote
 * @Description TODO
 * @Date 2022/9/15 17:04
 * @Created by 翊
 */
@Component
@FeignClient(name = "my-u-center", configuration = FeignConfiguration.class)
public interface UserRemote {

    @GetMapping("/remote/uc/user")
    UserVo getUser();

    @GetMapping("/remote/uc/permission-check")
    boolean adminPermission();
}
