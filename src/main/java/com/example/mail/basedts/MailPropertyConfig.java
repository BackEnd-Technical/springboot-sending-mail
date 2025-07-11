package com.example.mail.basedts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class MailPropertyConfig {
    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.server.host}")
    private String mailHost;

    @Value("${mail.server.port}")
    private Integer mailPort;

    @Value("${mail.server.protocol}")
    private String mailProtocol;

    @Value("${mail.server.username}")
    private String mailUserName;

    @Value("${mail.server.password}")
    private String mailPassword;

    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private String mailSmtpStarttlsEnable;

    @Value("${mail.smtp.quitwait}")
    private String mailSmtpQuitwait;

    @Value("${mail.template.content-type}")
    private String mailTemplateContentType;

    @Value("${mail.template.charset}")
    private String mailTemplateCharset;
}
