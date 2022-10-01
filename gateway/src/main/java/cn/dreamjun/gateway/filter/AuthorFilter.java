package cn.dreamjun.gateway.filter;

import cn.dreamjun.base.reponse.R;
import cn.dreamjun.base.vo.UserVo;
import cn.dreamjun.gateway.remote.UserRemote;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Classname AuthorFilter
 * @Description TODO
 * @Date 2022/9/22 14:04
 * @Created by 翊
 */
@Slf4j
@Component
public class AuthorFilter implements GlobalFilter {

    @Autowired
    private UserRemote userRemote;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 做过滤操作
        // 拿到请求路径
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 获取请求的 Path 路径，对 /admin/** 路径进行用户权限拦截
        URI uri = request.getURI();
        String path = uri.getPath();
        log.info("path ==> " + path);
        // 判断是不是管理员的资源
        if (antPathMatcher.match("/admin/**", path)) {
            // 从请求的 request 中的请求头 headers 中拿 token
            HttpHeaders headers = request.getHeaders();
            // 获取请求头部的 Cookie 所有的值
            List<String> list = headers.get("Cookie");
            String targetToken = null;
            // 如果 Cookie 为 null，说明用户未登录，直接不放行（返回没有权限）
            if (list == null) {
                // 不放行，返回没有权限
                return noPermission(response);
            }
            // 遍历所有 Cookie 的字符串，使用正则表达式获取指定的 firelight_token cookie 值
            for (String cookieStr : list) {
                //正则过滤
                // var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
                String regex = "(^| )firelight_token=([^;]*)(;|$)";
                Matcher matcher = Pattern.compile(regex).matcher(cookieStr);
                if (matcher.find()) {
                    String group = matcher.group();
                    //sob_token=xxxxx
                    targetToken = group.substring(group.indexOf('=') + 1).replace(";", "");
                }
            }
            log.info("target token ==> " + targetToken);
            // 获取到目标 targetToken 进行数据的检查
            if (targetToken == null) {
                // 不放行，返回没有权限
                return noPermission(response);
            }
            // 调用 parseToken 进行 Token 的解析返回 UserVo 对象，之后进行用户的身份认证
            UserVo userVo = userRemote.parseToken(targetToken);
            log.info("需要判断角色....{}", path);
            // 要进行判断当前用户是不是管理员
            // 如何判断当前用户是不是管理
            // 如果 token 已经过期导致 userVo == null 则不放行
            // 如果该用户的 roles 权限为 null 不放行
            // 如果该用户的 roles 全西安不包括 SUPER_ADMIN 不放行
            if (userVo == null || userVo.getRoles() == null || !userVo.getRoles().contains("SUPER_ADMIN")) {
                //没有权限
                log.info("permission denied...");
                // 不放行，返回没有权限
                return noPermission(response);
            }
            //放行
            log.info("permitted...");
        }
        return chain.filter(exchange);
    }

    /**
     * 拦截请求，返回拒绝访问的响应
     * @param response 响应
     * @return
     */
    private Mono<Void> noPermission(ServerHttpResponse response) {
        /*
            1、无权访问的响应 R 对象
            2、将 R 对象使用 Gson 对象的 toJSon() 方法进行数据格式的转换
            3、将转换后的 json result 对象写入 response 响应对象中
            4、设置 response 响应对象的响应头中的 Content-type 内容类型为
            5、写入 response 响应中的 headers 响应头中，同时采取 UTF-8 字符编码 application/json;charset=UTF-8 防止乱码
         */
        R responseContent = R.NO_PERMISSION();
        Gson gson = new Gson();
        String result = gson.toJson(responseContent);
        DataBuffer dataBuffer = response.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
        response.getHeaders().add("Content-type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(dataBuffer));
    }

}

