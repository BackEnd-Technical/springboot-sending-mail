package com.example.mail.common.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String message;
//    private String templateName;
//    private Map<String, Object> variables;
}
