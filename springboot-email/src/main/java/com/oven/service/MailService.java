package com.oven.service;

public interface MailService {

    /**
     * 发送简单文本的邮件
     */
    boolean send(String to, String subject, String content);

    /**
     * 发送 html 的邮件
     */
    boolean sendWithHtml(String to, String subject, String html);

    /**
     * 发送带有图片的 html 的邮件
     */
    boolean sendWithImageHtml(String to, String subject, String html, String[] cids, String[] filePaths);


    /**
     * 发送带有附件的邮件
     */
    boolean sendWithWithEnclosure(String to, String subject, String content, String[] filePaths);

}