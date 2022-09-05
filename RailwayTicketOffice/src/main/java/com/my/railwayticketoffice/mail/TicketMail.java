package com.my.railwayticketoffice.mail;

import com.itextpdf.text.DocumentException;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.receipt.PdfTicketReceipt;
import com.my.railwayticketoffice.receipt.TicketReceipt;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class that send mail to user with purchased tickets
 *
 * @author Yevhen Pashchenko
 */
public class TicketMail implements Mail {

    private static final Logger logger = LogManager.getLogger(TicketMail.class);
    private final TicketReceipt ticketReceipt = new PdfTicketReceipt();

    @Override
    public void send(User user, HttpSession httpSession) throws DocumentException, IOException, MessagingException {
        String locale = (String) httpSession.getAttribute("locale");
        ticketReceipt.create(user);
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
            message.setSubject("Tickets");
        } else {
            message.setSubject("Квитки");
        }
        String msg;
        if ("en".equals(locale)) {
            msg = "<h3>Hello, " + user.getFirstName() + " " + user.getLastName()
                    + ".</h3> There are a purchased ticket in attach file.";
        } else {
            msg = "<h3>Привіт, " + user.getFirstName() + " " + user.getLastName()
                    + ".</h3> У прикріпленому файлі знаходяться куплені квитки.";
        }
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        MimeBodyPart attachBodyPart = new MimeBodyPart();
        File attachedFile = new File(user.getId() + ".pdf");
        attachBodyPart.attachFile(attachedFile);
        multipart.addBodyPart(attachBodyPart);
        message.setContent(multipart);
        Transport.send(message);
        boolean isDeleted = attachedFile.delete();
        if (!isDeleted) {
            logger.info("Failed to delete file" + user.getId() + ".pdf with user tickets");
        }
    }
}
