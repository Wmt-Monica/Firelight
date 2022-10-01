package cn.dreamjun.base.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.beans.factory.ObjectFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Classname FeignConfiguration
 * @Description TODO
 * @Date 2022/9/22 14:44
 * @Created by 翊
 */
@Slf4j
public class FeignConfiguration implements RequestInterceptor {

    /**
     * 远程调用（发送 request）：客户端发送请求 -> moyu -> u-center
     * 其中 moyu -> u-center 的请求时独立的请求，因此我们需要转发，设置 Header，
     * 才能使得 moyu 远程调用 u-center 的请求中携带了客户端的 Header
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                template.header(name, values);
            }
        }
    }

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Decoder feignDecoder() {
        return new ResultStatusDecoder(
                new OptionalDecoder(
                        new ResponseEntityDecoder(
                                new SpringDecoder(this.messageConverters))));
    }

    static class ResultStatusDecoder implements Decoder {
        final Decoder delegate;

        public ResultStatusDecoder(Decoder delegate) {
            Objects.requireNonNull(delegate, "Decoder must not be null. ");
            this.delegate = delegate;
        }

        /**
         * 远程调用（返回 response）：u-center -> moyu -> 客户端
         * u-center 返回给 moyu 的 response 响应是一条独立的响应，
         * u-center 在该 response 设置的 cookie 需要额外去拦截远程调用的响应来进行设置
         * @param response
         * @param type
         * @return
         * @throws IOException
         */
        @Override
        public Object decode(Response response, Type type) throws IOException {
            // 判断是否返回参数是否是异常
            InputStream inputStream = response.body().asInputStream();
            String resultStr = IOUtils.toString(inputStream, "UTF-8");
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                HttpServletResponse targetResponse = attributes.getResponse();
                //把cookies给到返回的response
                Map<String, Collection<String>> headers = response.headers();
                Set<Map.Entry<String, Collection<String>>> entries = headers.entrySet();
                for (Map.Entry<String, Collection<String>> next : entries) {
                    if (next.getKey().equalsIgnoreCase("set-cookie")) {
                        Collection<String> value = next.getValue();
                        Iterator<String> iterator = value.iterator();
                        if (iterator.hasNext() && targetResponse != null) {
                            String tokenKey = iterator.next();
                            log.info("tokenKey ==> " + tokenKey);
                            targetResponse.setHeader("Set-Cookie", tokenKey);
                            break;
                        }
                    }
                }
            }
            return delegate.decode(response.toBuilder().body(resultStr, StandardCharsets.UTF_8).build(), type);
        }
    }
}

