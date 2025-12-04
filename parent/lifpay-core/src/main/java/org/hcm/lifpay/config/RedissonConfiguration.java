package org.hcm.lifpay.config;


import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.redis.host")
public class RedissonConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public Config redissonConfig(){
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s", redisProperties.getHost(), redisProperties.getPort());
        config.useSingleServer().setAddress(redisUrl)
                .setPassword(redisProperties.getPassword())
                .setDatabase(redisProperties.getDatabase());
        return config;
    }


}
