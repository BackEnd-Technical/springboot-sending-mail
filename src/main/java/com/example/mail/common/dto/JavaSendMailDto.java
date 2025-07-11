package com.example.mail.common.dto;

import lombok.Data;

@Data
public class JavaSendMailDto {
    private String mailTo;
    private String mailFrom;
    private String mailSubject;
    private String content;
}
