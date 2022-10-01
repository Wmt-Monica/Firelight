package cn.dreamjun.uc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @Classname AsyncConfiguration
 * @Description TODO
 * @Date 2022/9/27 15:20
 * @Created by 翊
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setKeepAliveSeconds(36000);
        executor.setThreadNamePrefix("firelight_task_worker-");
        executor.setQueueCapacity(30);
        executor.initialize();
        return executor;
    }

}
