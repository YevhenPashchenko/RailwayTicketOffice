package com.my.railwayticketoffice.mail;

import com.itextpdf.text.DocumentException;
import com.my.railwayticketoffice.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Interface that provides a method that send a mail
 *
 * @author Yevhen Pashchenko
 */
public interface Mail {

    /**
     * Method that send email.
     * @param user - {@link User}.
     * @param session - current session.
     */
    void send(User user, HttpSession session) throws IOException, MessagingException, DocumentException;
}
