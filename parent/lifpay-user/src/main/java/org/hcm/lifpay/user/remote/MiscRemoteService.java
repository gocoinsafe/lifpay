package org.hcm.lifpay.user.remote;


import org.hcm.lifpay.config.ServiceFeignConfiguration;
import org.hcm.lifpay.misc.MiscClient;
import org.springframework.cloud.openfeign.FeignClient;

//@FeignClient(value = "lifpay-misc", configuration = ServiceFeignConfiguration.class)
public interface MiscRemoteService extends MiscClient {

}
