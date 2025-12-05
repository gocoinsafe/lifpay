package org.hcm.lifpay.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "org.hcm.lifpay.aspect",
        "org.hcm.lifpay.config",
        "org.hcm.lifpay.handler",
        "org.hcm.lifpay.util"})
@Slf4j
public class CoreConfiguration {



}
