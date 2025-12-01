package org.hcm.lifpay.misc.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.service.FormService;
import org.hcm.lifpay.misc.service.MailService;
import org.hcm.lifpay.misc.vo.FormInfoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(value = "Lifpay表单", tags = "Lifpay表单")
@Slf4j
@RequestMapping("/misc")
public class FormController {

    @Autowired
    private FormService formService;

//    @Autowired
//    private MailService mailService;

    /**
     * 备注：此接口是面向外部全部用户的所以没有鉴权，属于高危风险接口！！！
     * */
    @ApiOperation(value = "提交表单接口")
    @PostMapping(path = "/form/submit")
    public BaseResponse<String> submitForm(@RequestBody FormInfoRequest request) {
        log.info("收到表单提交请求: {}", request);
        return formService.submitForm(request);
    }

    // 测试发送简单文本邮件
//    @GetMapping("/simple")
//    public String sendSimpleMail() {
//        try {
//            mailService.sendSimpleMail(
//                    "jerry@lifpay.me,aubrey@bittheory.us", // 收件人
//                    "测试简单邮件",       // 主题
//                    "这是 Spring Boot 发送的简单文本邮件，无需复杂配置～" // 正文
//            );
//            return "简单邮件发送成功！";
//        } catch (Exception e) {
//            return "发送失败：" + e.getMessage();
//        }
//    }


}
