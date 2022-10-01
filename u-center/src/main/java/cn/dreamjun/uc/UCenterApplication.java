package cn.dreamjun.uc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @Classname UCenterApplication
 * @Description TODO
 * @Date 2022/9/9 8:37
 * @Created by 翊
 */
@SpringBootApplication
@MapperScan("cn.dreamjun.uc.mapper")
@ComponentScan({"cn.dreamjun.common", "cn.dreamjun.uc"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 开启权限验证
public class UCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UCenterApplication.class, args);
    }
}
