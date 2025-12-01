package org.hcm.lifpay.misc.service;

import javax.mail.MessagingException;

public interface MailService {


    /**
     * 1. 发送简单文本邮件
     * */
    void sendSimpleMail(String to, String subject, String content);


    /**
     * 2.
     * */
    void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException;


    /**
     * 3. 发送带附件的邮件
     * */
    void sendAttachmentMail(String to, String subject, String content, String filePath, boolean isHtml) throws MessagingException;

}
