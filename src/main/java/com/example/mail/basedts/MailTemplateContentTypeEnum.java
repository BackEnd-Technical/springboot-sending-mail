package com.example.mail.basedts;

public enum MailTemplateContentTypeEnum {
    /** text/html */
    TEXT_HTML("text/html"),
    /** text/plain */
    TEXT_PLAIN("text/plain")

    ;

    MailTemplateContentTypeEnum(String value) {
        codeValue = value;
    }

    private String codeValue;

    public String getValue() {

        return codeValue;
    }
}