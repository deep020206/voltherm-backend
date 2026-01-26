package com.voltherm.service;

import com.voltherm.exception.ValidationException;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import java.util.Properties;
import java.util.regex.Pattern;

@Service
public class EmailConfigService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Value("${spring.mail.host:smtp.gmail.com}")
    private String mailHost;

    @Value("${spring.mail.port:587}")
    private int mailPort;

    private String senderEmail;
    private String senderPassword;
    private String receiverEmail;

    public EmailConfigService(
            @Value("${spring.mail.username:}") String senderEmail,
            @Value("${spring.mail.password:}") String senderPassword,
            @Value("${voltherm.email.company:}") String receiverEmail) {
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void updateReceiverEmail(String newReceiverEmail) {
        if (newReceiverEmail == null || newReceiverEmail.trim().isEmpty()) {
            throw new ValidationException("Receiver email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(newReceiverEmail).matches()) {
            throw new ValidationException("Invalid email format");
        }

        this.receiverEmail = newReceiverEmail;
    }

    public void updateSenderEmail(String newSenderEmail, String newAppPassword) {
        if (newSenderEmail == null || newSenderEmail.trim().isEmpty()) {
            throw new ValidationException("Sender email cannot be empty");
        }

        if (newAppPassword == null || newAppPassword.trim().isEmpty()) {
            throw new ValidationException("App password is required and cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(newSenderEmail).matches()) {
            throw new ValidationException("Invalid email format");
        }

        // Test the new credentials before applying
        try {
            testEmailCredentials(newSenderEmail, newAppPassword);
        } catch (MessagingException e) {
            throw new ValidationException("Failed to authenticate with provided credentials. Please verify your email and app password.");
        }

        this.senderEmail = newSenderEmail;
        this.senderPassword = newAppPassword;
    }

    public JavaMailSender createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(senderEmail);
        mailSender.setPassword(senderPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.transport.protocol", "smtp");

        return mailSender;
    }

    private void testEmailCredentials(String email, String password) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailHost);
        props.put("mail.smtp.port", mailPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.transport.protocol", "smtp");

        Session session = Session.getInstance(props);
        Transport transport = session.getTransport("smtp");
        
        try {
            transport.connect(mailHost, mailPort, email, password);
        } finally {
            transport.close();
        }
    }
}
