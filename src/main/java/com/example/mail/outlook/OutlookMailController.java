package com.example.mail.outlook;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/outlook")
public class OutlookMailController {

    @Autowired
    @Qualifier(value = "OutlockMailSender")
    private JavaMailSender outlookMailSender;

    @PostMapping("/simple-send")
    public void sendMailSimple(@RequestBody OutlookMailRequestDto mailRequestDto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailRequestDto.getFrom());
            message.setTo(mailRequestDto.getTo());
            message.setSubject(mailRequestDto.getSubject());
            message.setText(mailRequestDto.getContent());

            outlookMailSender.send(message);
            log.info("Email sent successfully to: {}", mailRequestDto.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to: {}", mailRequestDto.getTo(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @PostMapping("/html-send")
    public void sendHtmlEmail(@RequestBody OutlookMailRequestDto mailRequestDto) {
        try {
            MimeMessage message = outlookMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(mailRequestDto.getFrom());
            helper.setTo(mailRequestDto.getTo());
            helper.setSubject(mailRequestDto.getSubject());
            helper.setText(mailRequestDto.getHtmlContent(), true);

            if (StringUtils.isNotBlank(mailRequestDto.getAttactmentFile())) {
                FileSystemResource file = new FileSystemResource(new File(mailRequestDto.getAttactmentFile()));
                helper.addAttachment(file.getFilename(), file);
            }

            if (ArrayUtils.isNotEmpty(mailRequestDto.getCc())) {
                helper.setCc(mailRequestDto.getCc());
            }

            if (ArrayUtils.isNotEmpty(mailRequestDto.getBcc())) {
                helper.setBcc(mailRequestDto.getBcc());
            }

            outlookMailSender.send(message);
            log.info("HTML email sent successfully to: {}", mailRequestDto.getTo());
        } catch (Exception e) {
            log.error("Failed to send HTML email to: {}", mailRequestDto.getTo(), e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }
}
