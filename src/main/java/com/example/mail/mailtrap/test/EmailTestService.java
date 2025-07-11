//package com.example.mail.mailtrap.test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//import com.example.mail.mailtrap.MailtrapService;
//
//@Component
//public class EmailTestService {
//    @Autowired
//    private MailtrapService emailService;
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void sendTestEmail() {
//        // Test gửi email đơn giản
//        emailService.sendSimpleEmail("test@example.com", "Test Email from Spring Boot",
//                "This is a test email sent from Spring Boot using Mailtrap!");
//
//        // Test gửi email HTML
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("username", "John Doe");
//        variables.put("message", "Welcome to our application!");
//
//        emailService.sendHtmlEmail("test@example.com", "Welcome Email", "email-template", // template name
//                variables);
//    }
//}
