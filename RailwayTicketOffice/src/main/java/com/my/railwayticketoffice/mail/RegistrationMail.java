package com.my.railwayticketoffice.mail;

import com.my.railwayticketoffice.entity.User;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class that send mail to user for finish registration
 *
 * @author Yevhen Pashchenko
 */
public class RegistrationMail implements Mail {

    @Override
    public void send(User user, HttpSession httpSession) throws IOException, MessagingException {
        String locale = (String) httpSession.getAttribute("locale");
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
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        if ("en".equals(locale)) {
            message.setSubject("Confirm registration");
        } else {
            message.setSubject("Підтвердження реєстрації");
        }
        String msg;
        if ("en".equals(locale)) {
            msg = "<h3>Hello, " + user.getFirstName() + " " + user.getLastName()
                    + ".</h3> Follow the link to confirm registration. <a href='" + properties.getProperty("address") + "controller?command=confirmRegistration&email=" + user.getEmail() + "'>Confirm</a>";
        } else {
            msg = "<h3>Привіт, " + user.getFirstName() + " " + user.getLastName()
                    + ".</h3> Для підтвердження реєстрації перейдіть по ссилці. <a href='" + properties.getProperty("address") + "controller?command=confirmRegistration&email=" + user.getEmail() + "'>Підтвердити</a>";
        }
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        Transport.send(message);
    }
}
