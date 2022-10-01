package cn.dreamjun.my.config;

import cn.dreamjun.common.utils.Constants;
import com.google.gson.Gson;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname QNconfig
 * @Description TODO
 * @Date 2022/9/5 12:38
 * @Created by 翊
 */
@Configuration
public class QNconfig {

    @Value("${qiniu.accesskey}")
    private String ACCESSKEY;

    @Value("${qiniu.secretkey}")
    private String SECRETKEY;

    @Bean
    public com.qiniu.storage.Configuration qiniuConfig(){
        //设置存储区域
        return new com.qiniu.storage.Configuration(Region.region2());
    }

    @Bean
    public UploadManager uploadManager(){
        return new UploadManager(qiniuConfig());
    }

    /**
     * 获取授权
     * @return
     */
    @Bean
    public Auth auth(){
        return Auth.create(ACCESSKEY, SECRETKEY);
    }

    /**
     * 构建一个管理实例
     * @return
     */
    @Bean
    public BucketManager bucketManager(){
        return new BucketManager(auth(),qiniuConfig());
    }

    @Bean
    public Gson gson(){
        return new Gson();
    }
}
