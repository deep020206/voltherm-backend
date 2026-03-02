package com.voltherm.service;

import com.voltherm.model.Inquiry;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailConfigService emailConfigService;

    public void sendInquiryNotification(Inquiry inquiry) {
        try {
            JavaMailSender mailSender = emailConfigService.createMailSender();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailConfigService.getReceiverEmail());
            helper.setSubject("New Inquiry Received - " + inquiry.getName());
            helper.setText(buildInquiryEmailContent(inquiry), true);

            mailSender.send(message);
            logger.info("Inquiry notification email sent successfully for inquiry ID: {}", inquiry.getId());
        } catch (MessagingException e) {
            logger.error("Failed to send inquiry notification email for inquiry ID: {}", inquiry.getId(), e);
            // Don't throw exception to prevent inquiry submission failure
        }
    }

    private String buildInquiryEmailContent(Inquiry inquiry) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html>");
        content.append("<head>");
        content.append("<style>");
        content.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        content.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        content.append(".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }");
        content.append(".content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }");
        content.append(".field { margin-bottom: 15px; }");
        content.append(".label { font-weight: bold; color: #555; }");
        content.append(".value { margin-left: 10px; color: #000; }");
        content.append(".footer { margin-top: 20px; padding-top: 20px; border-top: 1px solid #ddd; text-align: center; color: #777; font-size: 12px; }");
        content.append("</style>");
        content.append("</head>");
        content.append("<body>");
        content.append("<div class='container'>");
        content.append("<div class='header'>");
        content.append("<h2>New Inquiry Received</h2>");
        content.append("</div>");
        content.append("<div class='content'>");
        
        content.append("<div class='field'>");
        content.append("<span class='label'>Inquiry ID:</span>");
        content.append("<span class='value'>").append(inquiry.getId()).append("</span>");
        content.append("</div>");
        
        content.append("<div class='field'>");
        content.append("<span class='label'>Name:</span>");
        content.append("<span class='value'>").append(inquiry.getName()).append("</span>");
        content.append("</div>");
        
        content.append("<div class='field'>");
        content.append("<span class='label'>Email:</span>");
        content.append("<span class='value'>").append(inquiry.getEmail()).append("</span>");
        content.append("</div>");
        
        content.append("<div class='field'>");
        content.append("<span class='label'>Phone:</span>");
        content.append("<span class='value'>").append(inquiry.getPhoneNumber() != null ? inquiry.getPhoneNumber() : "Not provided").append("</span>");
        content.append("</div>");

        content.append("<div class='field'>");
        content.append("<span class='label'>Company:</span>");
        String company = (inquiry.getCompany() != null && !inquiry.getCompany().isBlank())
                ? inquiry.getCompany() : "--";
        content.append("<span class='value'>").append(company).append("</span>");
        content.append("</div>");

        // Products table — only render when cart items are present
        if (inquiry.getCartItems() != null && !inquiry.getCartItems().isEmpty()) {
            content.append("<div class='field'>");
            content.append("<span class='label'>Products Enquired:</span>");
            content.append("<table style='width:100%;border-collapse:collapse;margin-top:10px;font-size:14px;font-family:Arial,sans-serif;'>");
            content.append("<thead>");
            content.append("<tr>");
            content.append("<th style='background-color:#4CAF50;color:#fff;text-align:center;padding:8px 10px;border:1px solid #45a049;width:5%;'>#</th>");
            content.append("<th style='background-color:#4CAF50;color:#fff;text-align:left;padding:8px 10px;border:1px solid #45a049;'>Product</th>");
            content.append("<th style='background-color:#4CAF50;color:#fff;text-align:center;padding:8px 10px;border:1px solid #45a049;width:10%;'>Qty</th>");
            content.append("</tr>");
            content.append("</thead>");
            content.append("<tbody>");
            int row = 1;
            for (Inquiry.CartItem ci : inquiry.getCartItems()) {
                String rowBg = (row % 2 == 0) ? "#f2f2f2" : "#ffffff";
                content.append("<tr style='background-color:").append(rowBg).append(";'>");
                content.append("<td style='padding:7px 10px;border:1px solid #ddd;text-align:center;color:#555;'>").append(row++).append("</td>");
                content.append("<td style='padding:7px 10px;border:1px solid #ddd;color:#333;'>").append(ci.getTitle() != null ? ci.getTitle() : "").append("</td>");
                content.append("<td style='padding:7px 10px;border:1px solid #ddd;text-align:center;color:#333;font-weight:bold;'>").append(ci.getQuantity()).append("</td>");
                content.append("</tr>");
            }
            content.append("</tbody>");
            content.append("</table>");
            content.append("</div>");
        }

        content.append("<div class='field'>");
        content.append("<span class='label'>Message:</span>");
        content.append("<div class='value' style='margin-top: 10px; padding: 10px; background-color: white; border-left: 3px solid #4CAF50;'>");
        content.append(inquiry.getRequirements() != null ? inquiry.getRequirements() : "No message provided");
        content.append("</div>");
        content.append("</div>");
        
        content.append("<div class='field'>");
        content.append("<span class='label'>Submitted At:</span>");
        content.append("<span class='value'>").append(formatter.format(inquiry.getCreatedAt())).append("</span>");
        content.append("</div>");
        
        content.append("</div>");
        content.append("<div class='footer'>");
        content.append("<p>This is an automated notification from Voltherm Inquiry System.</p>");
        content.append("</div>");
        content.append("</div>");
        content.append("</body>");
        content.append("</html>");
        
        return content.toString();
    }

    public boolean isEmailConfigured() {
        return emailConfigService.isConfigured();
    }

    public void sendOtpEmail(String username, String otp) {
        try {
            JavaMailSender mailSender = emailConfigService.createMailSender();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailConfigService.getReceiverEmail());
            helper.setSubject("Password Change OTP - Voltherm Admin");
            helper.setText(buildOtpEmailContent(username, otp), true);

            mailSender.send(message);
            logger.info("OTP email sent successfully for user: {}", username);
        } catch (MessagingException | MailException e) {
            logger.error("Failed to send OTP email for user: {}", username, e);
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    private String buildOtpEmailContent(String username, String otp) {
        StringBuilder content = new StringBuilder();
        content.append("<!DOCTYPE html>");
        content.append("<html>");
        content.append("<head>");
        content.append("<style>");
        content.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }");
        content.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; }");
        content.append(".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }");
        content.append(".content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }");
        content.append(".otp-box { background-color: #fff; border: 2px solid #4CAF50; padding: 20px; text-align: center; font-size: 32px; font-weight: bold; letter-spacing: 8px; margin: 20px 0; }");
        content.append(".warning { color: #ff5722; font-weight: bold; margin-top: 20px; }");
        content.append(".footer { margin-top: 20px; padding-top: 20px; border-top: 1px solid #ddd; text-align: center; color: #777; font-size: 12px; }");
        content.append("</style>");
        content.append("</head>");
        content.append("<body>");
        content.append("<div class='container'>");
        content.append("<div class='header'>");
        content.append("<h2>Password Change Request</h2>");
        content.append("</div>");
        content.append("<div class='content'>");
        content.append("<p>Hello <strong>").append(username).append("</strong>,</p>");
        content.append("<p>You have requested to change your password. Please use the following OTP to complete the process:</p>");
        content.append("<div class='otp-box'>").append(otp).append("</div>");
        content.append("<p>This OTP will expire in <strong>5 minutes</strong>.</p>");
        content.append("<p class='warning'>⚠️ If you did not request this password change, please ignore this email and ensure your account is secure.</p>");
        content.append("</div>");
        content.append("<div class='footer'>");
        content.append("<p>This is an automated message from Voltherm Admin System.</p>");
        content.append("</div>");
        content.append("</div>");
        content.append("</body>");
        content.append("</html>");
        
        return content.toString();
    }
}
