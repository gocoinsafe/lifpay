package org.hcm.lifpay.misc.service.impl;


import cn.hutool.extra.template.TemplateEngine;
import org.hcm.lifpay.misc.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailServiceImpl implements MailService {


    // Spring Boot 自动注入的邮件发送核心类
    @Autowired
    private JavaMailSender mailSender;

    // 发送者邮箱（与 application.yml 中配置的 username 一致）
    private String from = "jerry@lifpay.me"; // 可在 yml 中配置：spring.mail.from=你的邮箱@qq.com，然后通过 @Value("${spring.mail.from}") 注入

    // 可选：Thymeleaf 模板引擎（用于发送 HTML 模板邮件）
//    @Autowired(required = false)
//    private TemplateEngine templateEngine;

    /**
     * 1. 发送简单文本邮件
     * @param to      收件人邮箱（多个用逗号分隔，如 "a@qq.com,b@163.com"）
     * @param subject 邮件主题
     * @param content 邮件正文（纯文本）
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 发送者
        message.setTo(to.split(",")); // 收件人（支持多个）
        message.setSubject(subject); // 主题
        message.setText(content); // 正文
        mailSender.send(message); // 发送
    }

    /**
     * 2. 发送 HTML 格式邮件（支持图片、超链接等）
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param htmlContent HTML 正文（如 "<h1>验证码：123456</h1>"）
     */
    @Override
    public void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // true 表示支持多部分内容（HTML、图片、附件等）
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to.split(","));
        helper.setSubject(subject);
        // true 表示正文是 HTML 格式
        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }

    /**
     * 3. 发送带附件的邮件
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件正文（纯文本或 HTML）
     * @param filePath 附件路径（如 "D:/test.pdf" 或 "/Users/test.jpg"）
     * @param isHtml  是否为 HTML 正文
     */
    @Override
    public void sendAttachmentMail(String to, String subject, String content, String filePath, boolean isHtml) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to.split(","));
        helper.setSubject(subject);
        helper.setText(content, isHtml);

        // 添加附件（FileSystemResource 读取本地文件）
        File file = new File(filePath);
        if (file.exists()) {
            String fileName = file.getName();
            helper.addAttachment(fileName, new FileSystemResource(file));
        } else {
            throw new RuntimeException("附件文件不存在：" + filePath);
        }

        mailSender.send(mimeMessage);
    }



}
