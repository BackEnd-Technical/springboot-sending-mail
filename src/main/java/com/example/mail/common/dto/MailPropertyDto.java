package com.example.mail.common.dto;

import lombok.Data;

@Data
public class MailPropertyDto {

    private Mailtrap mailtrap = new Mailtrap();

    @Data
    public static class Mailtrap {
        private String host;
        private int port;
        private String username;
        private String password;
        private Smtp smtp = new Smtp();
    }

    @Data
    public static class Smtp {
        private boolean auth = true;
        private boolean quitwait = false;
        private Starttls starttls = new Starttls();
    }

    @Data
    public static class Starttls {
        private boolean enable = true;
    }

}
