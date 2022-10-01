package cn.dreamjun.my.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Classname SwaggerConfiguration
 * @Description TODO
 * @Date 2022/9/14 17:56
 * @Created by 翊
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean(value = "Firelight")
    @Order(value = 1)
    public Docket groupRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo())
                .select().apis(RequestHandlerSelectors.basePackage("cn.dreamjun.my.api"))
                .paths(PathSelectors.any()
                ).build();
    }

    private ApiInfo groupApiInfo() {
        return new ApiInfoBuilder()
                .title("萤火之光-翊")
                .description("Firelight 萤火之光管理中心")
                .termsOfServiceUrl("localhost")
                .licenseUrl("www.dreamjun.cn")
                .contact(new Contact("翊", "http://dreamjun.cn", "3040988158@qq.com"))
                .version("1.0").build();
    }

}
