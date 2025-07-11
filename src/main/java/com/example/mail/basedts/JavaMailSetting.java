package com.example.mail.basedts;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * メール設定
 */
@Getter
@Setter
public class JavaMailSetting {
    private final String from;
    private final List<String> to;
    private final List<String> cc;
    private final List<String> bcc;
    private final Date sentDate;

    private JavaMailSetting(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.cc = builder.cc;
        this.bcc = builder.bcc;
        this.sentDate = builder.sentDate;
    }

    public static FromSetter builder() {
        return new Builder();
    }

    public interface FromSetter {
        public abstract ToSetter setFrom(String from);
    }

    public interface ToSetter {
        public abstract ToSetter setTo(String to);

        public abstract ToSetter setTo(List<String> to);

        public abstract ToSetter setCc(String cc);

        public abstract ToSetter setCc(List<String> cc);

        public abstract ToSetter setBcc(String bcc);

        public abstract ToSetter setBcc(List<String> bcc);

        public abstract ToSetter setSentDate(Date sentDate);

        public abstract JavaMailSetting build();
    }

    private static class Builder implements FromSetter, ToSetter {
        private String from;
        private List<String> to;
        private List<String> cc;
        private List<String> bcc;
        private Date sentDate;

        @Override
        public Builder setFrom(String from) {
            this.from = from;
            return this;
        }

        @Override
        public Builder setTo(String to) {
            this.to = new ArrayList<>();
            if (!ObjectUtils.isEmpty(to)) {
                this.to.add(to);
            }
            return this;
        }

        @Override
        public Builder setTo(List<String> toList) {
            this.to = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(toList)) {
                for (String to : toList) {
                    if (!ObjectUtils.isEmpty(to)) {
                        this.to.add(to);
                    }
                }
            }

            return this;
        }

        @Override
        public Builder setCc(String cc) {
            this.cc = new ArrayList<>();
            if (!ObjectUtils.isEmpty(cc)) {
                this.cc.add(cc);
            }

            return this;
        }

        @Override
        public Builder setCc(List<String> ccList) {
            this.cc = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(ccList)) {
                for (String cc : ccList) {
                    if (!ObjectUtils.isEmpty(cc)) {
                        this.cc.add(cc);
                    }
                }
            }

            return this;
        }

        @Override
        public Builder setBcc(String bcc) {
            this.bcc = new ArrayList<>();
            if (!ObjectUtils.isEmpty(bcc)) {
                this.bcc.add(bcc);
            }

            return this;
        }

        @Override
        public Builder setBcc(List<String> bccList) {
            this.bcc = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(bccList)) {
                for (String bcc : bccList) {
                    if (!ObjectUtils.isEmpty(bcc)) {
                        this.bcc.add(bcc);
                    }
                }
            }

            return this;
        }

        @Override
        public Builder setSentDate(Date sentDate) {
            this.sentDate = sentDate;
            return this;
        }

        @Override
        public JavaMailSetting build() {
            return new JavaMailSetting(this);
        }
    }
}
