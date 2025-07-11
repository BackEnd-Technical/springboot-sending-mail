package com.example.mail.common.util;

import org.springframework.mail.SimpleMailMessage;

import com.example.mail.common.dto.JavaSendMailDto;

public class JavaSendMailUtil {

    public static SimpleMailMessage buildSimpleMailMessage(JavaSendMailDto dto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(dto.getMailTo());
        message.setSubject(dto.getMailSubject());
        message.setText(dto.getContent());
        message.setFrom(dto.getMailFrom());
        return message;
    }
}
