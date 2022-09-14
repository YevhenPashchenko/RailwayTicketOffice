package com.my.railwayticketoffice.receipt;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.my.railwayticketoffice.entity.Ticket;
import com.my.railwayticketoffice.entity.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Class that create a ticket receipt in pdf format.
 *
 * @author Yevhen Pashchenko
 */
public class PdfTicketReceipt implements TicketReceipt {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void create(User user) throws FileNotFoundException, DocumentException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Font font = FontFactory.getFont(Objects.requireNonNull(classLoader.getResource("fonts/TimesNewRoman.ttf")).toString(), BaseFont.IDENTITY_H, true);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(user.getId() + ".pdf"));
        document.open();

        for (Ticket ticket:
             user.getTickets()) {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidthPercentage(new float[] {27, 28, 25, 20}, new Rectangle(100, 100));

            List<String> cellsText = Arrays.asList("ЗАЛІЗНИЧНИЙ КВИТОК", ticket.getTicketNumber()
                    , "Railway Ticket Office", LocalDateTime.now().format(dateTimeFormatter));

            for (String text:
                 cellsText) {
                PdfPCell pdfPCell = new PdfPCell(new Paragraph(text, font));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setPadding(5);
                table.addCell(pdfPCell);
            }

            cellsText = Arrays.asList("Прізвище", ticket.getPassengerSurname(),
                    "Ім'я", ticket.getPassengerName(),
                    "Відправлення", ticket.getDepartureStation(),
                    "Поїзд", ticket.getTrainNumber(),
                    "Призначення", ticket.getDestinationStation(),
                    "Вагон/Тип", ticket.getCarriageNumber() + " " + ticket.getCarriageType(),
                    "Дата/Час відправлення", ticket.getDepartureDateTime().format(dateTimeFormatter),
                    "Місце", "" + ticket.getSeatNumber(),
                    "Дата/Час призначення", ticket.getDestinationDateTime().format(dateTimeFormatter),
                    "Вартість", ticket.getCost() + " грн.");

            for (int i = 0; i < cellsText.size(); i++) {
                PdfPCell pdfPCell = new PdfPCell(new Paragraph(cellsText.get(i), font));
                if (i % 2 != 0) {
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                }
                pdfPCell.setPadding(5);
                table.addCell(pdfPCell);
            }

            table.setSpacingAfter(20);
            document.add(table);
        }

        document.close();
    }
}
