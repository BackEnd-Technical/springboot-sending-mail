package com.example.mail.common;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.mail.basedts.CustomJavaMailSenderImpl;

@Configuration
public class MailSpringContextBeansConfig {

    @Bean(name = "JavaMailSenderMailtrap")
    public JavaMailSender getJavaMailSender() {
        CustomJavaMailSenderImpl mailSender = new CustomJavaMailSenderImpl();
        mailSender.setHost("sandbox.smtp.mailtrap.io");
        mailSender.setPort(2525);
        mailSender.setUsername("c19f3ad6797901");
        mailSender.setPassword("11b2d4c922f8eb");

        Properties javaMailProperties = mailSender.getJavaMailProperties();
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.starttls.enable", true);

        return mailSender;
    }
}
