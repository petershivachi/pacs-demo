package com.shivachi.pacs.demo.utilities.mailutil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {
    @Value("${spring.mail.sender}")
    private String fromEmail;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.sender}")
    private String userName;
    @Value("${spring.mail.password}")
    private String password;

    public boolean sendEmail(String subject, String username, String message, String toEmail){
        AtomicBoolean response = new AtomicBoolean(false);

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.ssl.trust","mail.man.com");

        log.info("Gmail username: {}", userName);
        log.info("Gmail password: {}", password);

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail, "No-reply"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail, String.format("Dear %s", username)));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
            log.info("Email sent successfully....");
            response.set(true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Exception while sending email {}", e.getMessage());
            response.set(false);
        }

        return response.get();
    }
}
