package com.example.mail.basedts;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class JavaSendMailService {

    private final MailPropertyConfig mailConfig;

    private final JavaMailSender mailSender;

    public JavaSendMailService(@Autowired @Qualifier(value = "JavaMailSenderDtsBase") JavaMailSender mailSender,
            @Autowired MailPropertyConfig mailConfig) {
        this.mailSender = mailSender;
        this.mailConfig = mailConfig;
    }

    public void sendMailSample() throws Exception {
        JavaMailSetting mailSetting = JavaMailSetting.builder().setFrom(mailConfig.getMailFrom())
                .setTo("vinh.nt@dtsvn.com").build();

        MailTemplateContentTypeEnum contentType = MailTemplateContentTypeEnum.TEXT_PLAIN;
        if (MailTemplateContentTypeEnum.TEXT_HTML.getValue().equals(mailConfig.getMailTemplateContentType())) {
            contentType = MailTemplateContentTypeEnum.TEXT_HTML;
        }

        Charset charset = StandardCharsets.UTF_8;
        if (!ObjectUtils.isEmpty(mailConfig.getMailTemplateCharset())) {
            charset = Charset.forName(mailConfig.getMailTemplateCharset());
        }

        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("SYSTEM_NAME", "SPRING MAIL");
        paramMap.put("TITLE", "Mail sample!");
        paramMap.put("propertyName", "Project Demo");
        paramMap.put("receiveDateAndTime", LocalDateTime.now().toString());
        paramMap.put("notificationContent", "Test Notification");

        SimpleMailMessage smm = this.getSimpleMailMessage("SampleMail", contentType, charset, paramMap, mailSetting);

        SendMailUtil.send(mailSender, smm, MailTemplateContentTypeEnum.TEXT_HTML, mailConfig.getMailFrom());
    }

    private SimpleMailMessage getSimpleMailMessage(String mailTemplate, MailTemplateContentTypeEnum contentType,
            Charset charset, Map<String, ?> paramMap, JavaMailSetting mailSetting) throws Exception {
        SimpleMailMessage smm = SendMailUtil.build(mailTemplate, contentType, charset, paramMap);

        smm.setFrom(mailSetting.getFrom());

        boolean hasRecipient = false;

        List<String> toList = mailSetting.getTo();
        if (CollectionUtils.isNotEmpty(toList)) {
            String[] toArray = toList.toArray(String[]::new);
            smm.setTo(toArray);
            hasRecipient = true;
        }

        List<String> ccList = mailSetting.getCc();
        if (CollectionUtils.isNotEmpty(ccList)) {
            String[] ccArray = ccList.toArray(String[]::new);
            smm.setCc(ccArray);
            hasRecipient = true;
        }

        List<String> bccList = mailSetting.getBcc();
        if (CollectionUtils.isNotEmpty(bccList)) {
            String[] bccArray = bccList.toArray(String[]::new);
            smm.setBcc(bccArray);
            hasRecipient = true;
        }

        if (hasRecipient == false) {
            Exception exception = new Exception("Không có người nhận!");
            throw exception;
        }

        smm.setSentDate(mailSetting.getSentDate());

        return smm;
    }

}
