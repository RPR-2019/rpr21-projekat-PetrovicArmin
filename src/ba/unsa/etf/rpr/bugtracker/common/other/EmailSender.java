package ba.unsa.etf.rpr.bugtracker.common.other;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Properties;

public class EmailSender {
    private static EmailSender emailSender = null;
    static final String serverMailAdress = "bugtracker123321@gmail.com";
    static final String serverMailPassword = "rfidwzqpcjmoutrp";
    static final String host = "smtp.gmail.com";
    static Properties properties;

    private EmailSender() {
        properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
    }

    public static void sendEmail(String receiverMail, String bugTitle, String solverUsername) {
        if (emailSender == null)
            emailSender = new EmailSender();

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(serverMailAdress, serverMailPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(serverMailAdress));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverMail));

            // Set Subject: header field
            message.setSubject("Your bug has been solved!");

            // Now set the actual message
            message.setContent("User <strong>" + solverUsername + "</strong> solved your bug \"<strong>" + bugTitle + "</strong>\" on date: " + LocalDate.now() + " !", "text/html");

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
