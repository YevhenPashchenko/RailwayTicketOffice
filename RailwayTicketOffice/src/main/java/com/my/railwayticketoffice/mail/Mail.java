package com.my.railwayticketoffice.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

import java.io.IOException;

/**
 * Interface that provides a method that send a mail
 *
 * @author Yevhen Pashchenko
 */
public interface Mail {

    /**
     * Method that send email.
     * @param email - user email.
     */
    void send(String email, String locale) throws IOException, MessagingException;
}
