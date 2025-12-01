package org.hcm.lifpay;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@Slf4j
public class LifpayGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifpayGatewayApplication.class, args);
        log.info("=======================Gateway Application is run ....========================");
    }
}
