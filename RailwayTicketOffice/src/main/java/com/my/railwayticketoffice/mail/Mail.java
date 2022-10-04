package com.my.railwayticketoffice.mail;

import com.itextpdf.text.DocumentException;
import com.my.railwayticketoffice.entity.User;
import jakarta.mail.MessagingException;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Interface that provides a method that send a mail
 *
 * @author Yevhen Pashchenko
 */
public interface Mail {

    /**
     * Method that send email.
     * @param users - list of {@link User}.
     * @param session - current session.
     */
    void send(List<User> users, HttpSession session) throws IOException, MessagingException, DocumentException;
}
