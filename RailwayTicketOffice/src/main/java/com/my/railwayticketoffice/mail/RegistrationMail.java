package com.my.railwayticketoffice.mail;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class RegistrationMail implements Mail {

    @Override
    public void send(String userEmail, String locale) throws IOException, MessagingException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("mail.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("mail.smtp.user"), properties.getProperty("mail.smtp.password"));
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(properties.getProperty("mail.smtp.bot")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
        if ("en".equals(locale)) {
            message.setSubject("Confirm user registration");
        } else {
            message.setSubject("Підтвердження реєстрації користувача");
        }
        String msg;
        if ("en".equals(locale)) {
            msg = "Follow the link to confirm registration <a href='http://localhost:8080/RailwayTicketOffice/controller?command=mainPage'>confirm</a>";
        } else {
            msg = "Для підтвердження реєстрації перейдіть по ссилці <a href='http://localhost:8080/RailwayTicketOffice/controller?command=mainPage'>підтвердити</a>";
        }
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        Transport.send(message);
    }
}
