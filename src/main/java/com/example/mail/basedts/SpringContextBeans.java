package com.example.mail.basedts;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class SpringContextBeans {

    private final MailPropertyConfig mailConfig;

    public SpringContextBeans(@Autowired MailPropertyConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    @Bean(name = "JavaMailSenderDtsBase")
    public JavaMailSender getJavaMailSender() {
        CustomJavaMailSenderImpl mailSender = new CustomJavaMailSenderImpl();
        mailSender.setHost(mailConfig.getMailHost());
        mailSender.setPort(mailConfig.getMailPort());
        mailSender.setUsername(mailConfig.getMailUserName());
        mailSender.setPassword(mailConfig.getMailPassword());
        mailSender.setProtocol(mailConfig.getMailProtocol());

        Properties javaMailProperties = mailSender.getJavaMailProperties();
        javaMailProperties.put("mail.smtp.auth", mailConfig.getMailSmtpAuth());
        javaMailProperties.put("mail.smtp.starttls.enable", mailConfig.getMailSmtpStarttlsEnable());
        javaMailProperties.put("mail.smtp.quitwait", mailConfig.getMailSmtpQuitwait());

        return mailSender;
    }

}
