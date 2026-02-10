package org.hcm.lifpay.misc.filter;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {


    @Bean
    public CorsFilter corsFilter() {
        // 1. 配置跨域信息
        CorsConfiguration config = new CorsConfiguration();
        // 允许前端的源（联调期间可以设为 *，允许所有前端访问；生产环境改为前端实际域名）
        config.addAllowedOriginPattern("*");
        // 允许的请求方法（GET/POST/PUT/DELETE等）
        config.addAllowedMethod("*");
        // 允许的请求头（如Content-Type、Token等）
        config.addAllowedHeader("*");
        // 允许携带Cookie（如果前端需要传Cookie，必须设为true）
        config.setAllowCredentials(true);
        // 跨域请求有效期（单位：秒，避免频繁预检请求）
        config.setMaxAge(3600L);

        // 生产环境示例：仅允许指定前端域名访问
//        config.addAllowedOriginPattern("http://192.168.1.100:8081"); // 前端IP+端口
//        config.addAllowedOriginPattern("https://frontend.lifpay.me");

        // 2. 配置哪些接口生效（/** 表示所有接口）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        // 3. 返回CORS过滤器
        return new CorsFilter(source);
    }
}
