package org.hcm.lifpay.misc;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAsync
@MapperScan("org.hcm.lifpay.misc.dao.repository")
@Slf4j
public class LifpayMiscApplication {



    public static void main(String[] args) {
        SpringApplication.run(LifpayMiscApplication.class, args);

        log.info("=======================misc Application is run ...========================");
    }

}
