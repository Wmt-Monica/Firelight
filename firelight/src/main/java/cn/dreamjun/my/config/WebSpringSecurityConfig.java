package cn.dreamjun.my.config;

import cn.dreamjun.my.remote.UserRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Classname WebSpringSecurityConfig
 * @Description TODO
 * @Date 2022/9/10 16:41
 * @Created by 翊
 */
@Configuration
@EnableWebSecurity
public class WebSpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRemote userRemote;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        String adminId = userRemote.getAdminUserPassword();
        //所有都放行
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .and().csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder createBCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
