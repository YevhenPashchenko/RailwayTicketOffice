package com.my.railwayticketoffice.receipt;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.my.railwayticketoffice.entity.Ticket;
import com.my.railwayticketoffice.entity.User;

import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;

/**
 * Interface that provides a method that create a ticket receipt
 *
 * @author Yevhen Pashchenko
 */
public interface TicketReceipt {

    /**
     * @param user that collect a list of {@link Ticket}.
     */
    void create(User user) throws FileNotFoundException, DocumentException;
}
