package org.hcm.lifpay.misc.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.hcm.lifpay.misc.common.MiscResultEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * swagger配置
 *
 * @author xinzhe
 */
@Configuration
//@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

//    @Bean(value = "miscErrCodeApi")
//    @Order(value = 1)
//    public Docket errCodeRestApi() {
//        //添加全局响应状态码
//        List<ResponseMessage> responseMessageList = new ArrayList<>();
//        Arrays.stream(MiscResultEnum.values()).forEach(errorEnum -> responseMessageList.add(
//                new ResponseMessageBuilder()
//                        .code(errorEnum.getCode())
//                        .message(errorEnum.getDesc())
//                        .responseModel(new ModelRef(errorEnum.getDesc()))
//                        .build()
//        ));
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(groupApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("org.hcm.lifpay.misc.controller"))
//                .paths(PathSelectors.ant("/misc/errorCode"))
//                .build()
//                .groupName("misc服务全局错误码")
//                .apiInfo(groupApiInfo())
//                .globalResponseMessage(RequestMethod.DELETE, responseMessageList);
//
//    }
//
//    @Bean(value = "innerApi")
//    @Order(value = 1)
//    public Docket innerRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(groupApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("org.hcm.lifpay.misc.controller.inner"))
//                .paths(PathSelectors.ant("/inner/misc/**"))
//                .build()
//                .groupName("misc服务内部接口")
//                .apiInfo(groupApiInfo());
//
//    }
//
//    @Bean(value = "miscApi")
//    @Order(value = 1)
//    public Docket groupRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(groupApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("org.hcm.lifpay.misc.controller"))
//                .paths(PathSelectors.ant("/wapi/message/**"))
//                .build()
//                .groupName("misc服务对外接口")
//                .apiInfo(groupApiInfo());
//
//    }
//
//    @Bean(value = "allApi")
//    @Order(value = 1)
//    public Docket groupAllRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(groupApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("org.hcm.lifpay.misc.controller"))
//                .build()
//                .groupName("misc服务所有接口")
//                .apiInfo(groupApiInfo());
//    }
//
//    private ApiInfo groupApiInfo() {
//        return new ApiInfoBuilder()
//                .title("Misc服务API接口文档")
//                .description("<div style='font-size:14px;color:red;'>Misc服务API接口文档</div>")
//                .termsOfServiceUrl("http://www.group.com/")
//                .version("1.0")
//                .build();
//    }



    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("misc服务API文档")
                        .description("Swagger3 接口文档（Spring Boot 2.6.11）")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Lifpay Team")));
    }



}
