package com.example.mail.basedts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class JavaMailController {

    @Autowired
    private JavaSendMailService javaSendMailService;

    @GetMapping("/dts-base-send-mail")
    public void sendMail() {
        try {
            javaSendMailService.sendMailSample();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
