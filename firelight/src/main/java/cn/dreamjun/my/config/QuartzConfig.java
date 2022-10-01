package cn.dreamjun.my.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname QuartzConfig
 * @Description TODO
 * @Date 2022/9/27 9:12
 * @Created by 翊
 */
@Configuration

public class QuartzConfig {

    private static final String LIKE_TASK_IDENTITY = "LikeTaskQuartz";

//    @Bean
//    public JobDetail quartzDetail() {
//        return JobBuilder.newJob(CommentThumbUpTask.class).withIdentity(LIKE_TASK_IDENTITY).storeDurably().build();
//    }

//    @Bean
//    public Trigger quartzTrigger() {
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                // .withIntervalInSeconds(10) //设置时间周期单位秒
//                .withIntervalInHours(2) //两个小时执行一次
//                .repeatForever();
//        return TriggerBuilder.newTrigger().forJob(quartzDetail())
//                .withIdentity(LIKE_TASK_IDENTITY)
//                .withSchedule(scheduleBuilder)
//                .build();
//    }
}
