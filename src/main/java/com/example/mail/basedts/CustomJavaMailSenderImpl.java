package com.example.mail.basedts;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.ObjectUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomJavaMailSenderImpl extends JavaMailSenderImpl {

    private String mailSignature;

    public CustomJavaMailSenderImpl() {
        super();
        this.setDefaultEncoding("UTF-8");
    }

    public void applyMailSignature(SimpleMailMessage message) {
        String text = message.getText();
        if (ObjectUtils.isEmpty(this.mailSignature)) {
            // 署名が設定されていない場合は、処理しない
            return;
        }
        String newText = text.concat("\n").concat(this.mailSignature);
        message.setText(newText);
    }
}
