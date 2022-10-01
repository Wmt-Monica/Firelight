package cn.dreamjun.my;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @Classname MoYuApplication
 * @Description TODO
 * @Date 2022/9/14 17:18
 * @Created by 翊
 */
@SpringBootApplication
@MapperScan("cn.dreamjun.my.mapper")
@ComponentScan({"cn.dreamjun.common", "cn.dreamjun.my", "cn.dreamjun.base"})
@EnableDiscoveryClient
@EnableFeignClients
public class MoYuApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoYuApplication.class, args);
    }
}
