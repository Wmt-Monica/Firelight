package cn.dreamjun.gateway.remote;

import cn.dreamjun.base.config.FeignConfiguration;
import cn.dreamjun.base.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Classname UserRemote
 * @Description TODO
 * @Date 2022/9/22 14:15
 * @Created by 翊
 */
@Component
@FeignClient(name = "my-u-center", configuration = FeignConfiguration.class)
public interface UserRemote {

    @GetMapping("/remote/uc/user")
    UserVo getUser();


    @GetMapping("/remote/uc/token")
    UserVo parseToken(@RequestParam("token") String token);

}
