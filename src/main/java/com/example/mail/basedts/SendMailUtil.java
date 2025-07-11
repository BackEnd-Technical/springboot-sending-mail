package com.example.mail.basedts;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import groovy.text.GStringTemplateEngine;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SendMailUtil {

    private static final String MAIL_SMTP_FROM = "mail.smtp.from";

    public static SimpleMailMessage build(String mailTemplate, MailTemplateContentTypeEnum contentType, Charset charset,
            Map<String, ?> parameter) {
        MessageBuilder builder = new MessageBuilder(mailTemplate, parameter);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(builder.getSubject(contentType, charset));
        message.setText(builder.getText(contentType, charset));
        return message;
    }

    public static SimpleMailMessage buildText(String mailTemplate, MailTemplateContentTypeEnum contentType,
            Charset charset, Map<String, ?> parameter) {
        MessageBuilder builder = new MessageBuilder(mailTemplate, parameter);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText(builder.getText(contentType, charset));
        return mailMessage;
    }

    public static SimpleMailMessage buildSubject(String mailTemplate, MailTemplateContentTypeEnum contentType,
            Charset charset, Map<String, ?> parameter) {
        MessageBuilder builder = new MessageBuilder(mailTemplate, parameter);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject(builder.getSubject(contentType, charset));
        return mailMessage;
    }

    public static SimpleMailMessage buildEmpty() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        return mailMessage;
    }

    public static String send(JavaMailSender sender, SimpleMailMessage simpleMessage,
            MailTemplateContentTypeEnum contentType) throws Exception {
        Assert.notNull(sender, "Spring Frameworkが提供しているメール送信機能が設定されていない");
        Assert.notNull(simpleMessage, "メールメッセージが設定されていない");
        return doSend(sender, simpleMessage, contentType, null);
    }

    public static String send(JavaMailSender sender, SimpleMailMessage simpleMessage,
            MailTemplateContentTypeEnum contentType, String bounceAddress) throws Exception {
        Assert.notNull(sender, "Spring Frameworkが提供しているメール送信機能が設定されていない");
        Assert.notNull(simpleMessage, "メールメッセージが設定されていない");
        Assert.hasText("バウンスメールの送信先に無効な設定がされている", bounceAddress);
        return doSend(sender, simpleMessage, contentType, bounceAddress);
    }

    private static String doSend(JavaMailSender sender, SimpleMailMessage simpleMessage,
            MailTemplateContentTypeEnum contentType, String bounceAddress) throws Exception {
        assertionPreDoSend(simpleMessage);

        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
            MimeMailMessage mimeMailMessage = new MimeMailMessage(helper);
            simpleMessage.copyTo(mimeMailMessage);
            if (MailTemplateContentTypeEnum.TEXT_HTML.equals(contentType)) {
                helper.setText(simpleMessage.getText(), true);
            }
            if (bounceAddress == null) {
                sender.send(mimeMessage);
            } else {
                send(sender, mimeMessage, new InternetAddress(bounceAddress, true));
            }
            log.info("メール送信しました。バウンスメールアドレス: {}, メッセージ情報(本文を除く): {}", bounceAddress != null ? bounceAddress : "設定無し",
                    new ReflectionToStringBuilder(simpleMessage, ToStringStyle.NO_CLASS_NAME_STYLE)
                            .setExcludeFieldNames("text").build());
            return mimeMessage.getMessageID();
        } catch (MailException | MessagingException e) {
            Exception exception = new Exception("メール送信に失敗しました", e);
            log.warn(exception.getMessage(), exception);
            throw exception;
        }
    }

    static void send(JavaMailSender sender, MimeMessage mimeMessage, Address bounceAddress) throws MailException {
        Properties prop = new Properties();
        prop.setProperty(MAIL_SMTP_FROM, bounceAddress.toString());
        JavaMailSender newSender = createNewSessionSender(sender, prop);
        newSender.send(mimeMessage);
    }

    static void assertionPreDoSend(SimpleMailMessage simpleMessage) {
        Assert.hasText("メールの送信者(From)が設定されていない。もしくは無効な設定がされている", simpleMessage.getFrom());
        boolean hasRecipient = false;
        for (String address : ArrayUtils.nullToEmpty(simpleMessage.getTo())) {
            Assert.hasText("メールの受信者(To)に無効な設定がされている", address);
            hasRecipient = true;
        }
        for (String address : ArrayUtils.nullToEmpty(simpleMessage.getCc())) {
            Assert.hasText("メールの受信者(Cc)に無効な設定がされている", address);
            hasRecipient = true;
        }
        for (String address : ArrayUtils.nullToEmpty(simpleMessage.getBcc())) {
            Assert.hasText(address, "メールの受信者(Bcc)に無効な設定がされている");
            hasRecipient = true;
        }
        Assert.isTrue(hasRecipient, "メールの受信者(To, Cc, Bcc)が設定されていない");
    }

    static JavaMailSender createNewSessionSender(JavaMailSender sender, Properties prop) {
        JavaMailSenderImpl originalSender = (JavaMailSenderImpl) sender;
        JavaMailSenderImpl newSessionSender = new JavaMailSenderImpl();

        try {
            // コンテナから取得したインスタンスから設定値をコピーする
            for (Map.Entry<Object, Object> entry : new BeanMap(originalSender).entrySet()) {
                String key = entry.getKey().toString();
                // 設定が不要なプロパティをスキップする
                switch (key) {
                case "class":
                case "session":
                case "javaMailProperties":
                case "defaultFileTypeMap":
                    if (log.isTraceEnabled()) {
                        log.trace("skip property: {}", key);
                    }
                    continue;
                default:
                    break;
                }

                BeanUtils.copyProperty(newSessionSender, key, entry.getValue());
                if (log.isDebugEnabled()) {
                    log.debug("put key: {} value: {}", key, entry.getValue());
                }
            }

            Properties newProperties = new Properties();
            // 異なるメールセッションを保持する為、コンテナから取得したインスタンスからプロパティをコピーする
            newProperties.putAll(originalSender.getJavaMailProperties());
            // アプリケーションで上書きするプロパティ値を上書きする
            newProperties.putAll(prop);
            if (log.isDebugEnabled()) {
                StringWriter writer = new StringWriter();
                try (PrintWriter pw = new PrintWriter(writer)) {
                    newProperties.list(pw);
                }
                log.debug("異なるメールセッションでメール送信する際のJavaMailPropertiesの設定値\n{}", writer.toString());
            }

            newSessionSender.setJavaMailProperties(newProperties);
            return newSessionSender;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected static class MessageBuilder {

        private static final String MAIL_TEMPLATES_DIR = "/mailTemplates/";

        protected final String mailTemplate;

        protected final Map<String, ?> parameter;

        @SuppressWarnings("serial")
        static class TemplateParameter extends HashMap<String, Object> {

            TemplateParameter(Map<String, ?> m) {
                super(m);
            }

            @Override
            public Object get(Object key) {

                if (!super.containsKey(key)) {
                    if (log.isWarnEnabled()) {
                        log.warn("メールテンプレートのマージ処理で、マップに存在しないキーが参照されました: {}", key);
                    }
                    return "";
                }

                Object val = super.get(key);
                if (val == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("メールテンプレートのマージ処理で、マップのキーに関連付けられた値がnullになっています: {}", key);
                    }

                    return "";
                }
                return val;
            }
        }

        protected MessageBuilder(String mailTemplate, Map<String, ?> parameter) {
            Assert.notNull(mailTemplate, "メールテンプレートを設定してください");
            Assert.notEmpty(parameter, "パラメータを設定してください");

            this.mailTemplate = mailTemplate;
            this.parameter = new TemplateParameter(parameter);
        }

        protected URL getSubjectTemplateFile(MailTemplateContentTypeEnum contentType) {

            return getMailTemplateFile("subject", contentType);
        }

        protected URL getTextTemplateFile(MailTemplateContentTypeEnum contentType) {

            return getMailTemplateFile("text", contentType);
        }

        protected URL getMailTemplateFile(String part, MailTemplateContentTypeEnum contentType) {

            Assert.notNull(part, "パートを設定してください");

            String extension = "gtpl";
            if (MailTemplateContentTypeEnum.TEXT_HTML.equals(contentType)) {
                extension = "html";
            }

            String resourceFilename = String.format("%s.%s.%s", mailTemplate, part, extension);

            URL url = getClass().getResource(MAIL_TEMPLATES_DIR + resourceFilename);

            if (url == null) {

                throw new IllegalStateException(String.format("メールテンプレートファイル%sが見つかりませんでした", resourceFilename));
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("メールテンプレートファイル{}が見つかりました", resourceFilename);
                }
            }

            return url;
        }

        protected String getSubject(MailTemplateContentTypeEnum contentType, Charset charset) {

            String subject = this.mergeMailTemplate(getSubjectTemplateFile(contentType), charset);

            return subject.trim();
        }

        protected String getText(MailTemplateContentTypeEnum contentType, Charset charset) {

            return this.mergeMailTemplate(getTextTemplateFile(contentType), charset);
        }

        private String mergeMailTemplate(URL templateFile, Charset charset) {

            GStringTemplateEngine engine = new GStringTemplateEngine();

            try {
                return engine.createTemplate(templateFile, charset).make(parameter).toString();
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
