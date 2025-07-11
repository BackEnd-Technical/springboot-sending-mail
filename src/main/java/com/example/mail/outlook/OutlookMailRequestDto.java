package com.example.mail.outlook;

import lombok.Data;

@Data
public class OutlookMailRequestDto {

    private String from;
    private String to;
    private String subject;
    private String content;
    private String htmlContent;
    private String attactmentFile;
    private String[] cc;
    private String[] bcc;
}
