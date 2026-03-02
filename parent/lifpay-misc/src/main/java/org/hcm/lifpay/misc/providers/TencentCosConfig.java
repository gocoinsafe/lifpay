package org.hcm.lifpay.misc.providers;


import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云COS配置类
 */
@RefreshScope
@Configuration
public class TencentCosConfig {

    @Value("${misc.cos.secrId}")
    private String secretId;
    @Value("${misc.cos.secretKey}") // 修复：原用了secretId的key
    private String secretKey;

    @Value("${misc.cos.region: ap-singapore}")
    private String region;


    /**
     * 初始化COS客户端（单例，避免重复创建）
     */
    @Bean
    public COSClient cosClient() {
        // 1. 初始化凭证
        COSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);
        // 2. 配置地域
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3. 创建COS客户端
        return new COSClient(credentials, clientConfig);
    }
}
