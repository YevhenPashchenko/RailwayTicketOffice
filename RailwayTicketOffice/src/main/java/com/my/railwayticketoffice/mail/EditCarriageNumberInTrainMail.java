package com.my.railwayticketoffice.mail;

import com.my.railwayticketoffice.entity.User;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class EditCarriageNumberInTrainMail implements Mail {

    @Override
    public void send(List<User> users, HttpSession httpSession) throws IOException, MessagingException {
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
        if ("en".equals(locale)) {
            message.setSubject("Carriage number in train change");
        } else {
            message.setSubject("Зміна номера вагона в поїзді");
        }
        for (User user:
                users) {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            String msg;
            if ("en".equals(locale)) {
                msg = "<h3>Hello, " + user.getFirstName() + " " + user.getLastName() + ".</h3>" +
                        "<div>We inform you that in train number" + user.getTickets().get(0).getTrainNumber() +
                        ", for which you purchased a ticket, your carriage number has changed from " +
                        user.getTickets().get(0).getCarriageNumber() + " to " + user.getTickets().get(1).getCarriageNumber() +
                        ". The ticket remains valid.</div>";
            } else {
                msg = "<h3>Привіт, " + user.getFirstName() + " " + user.getLastName() + ".</h3>" +
                        "<div>Повідомляємо вас, що у поїзді №" + user.getTickets().get(0).getTrainNumber() +
                        ", на який ви придбали квиток, змінився номер вагона з " + user.getTickets().get(0).getCarriageNumber() +
                        " на " + user.getTickets().get(1).getCarriageNumber() + ". Квиток залишається дійсним.</div>";
            }
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        }
    }
}
