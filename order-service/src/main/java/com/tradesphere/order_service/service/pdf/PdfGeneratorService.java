package com.tradesphere.order_service.service.pdf;


import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class PdfGeneratorService {

    public byte[] generatePdf() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font bodyFont = new Font(Font.HELVETICA, 12);
        try {
            Image logo = Image.getInstance("src/main/resources/Image1.jpg");
            logo.scaleAbsolute(100, 50);
            document.add(logo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.add(new Paragraph("Spring Boot PDF Generation", titleFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("This PDF is generated dynamically using OpenPDF.", bodyFont));

        document.add(new Paragraph(" "));
        document.add(createTable());



        document.close();
        return outputStream.toByteArray();
    }

    private PdfPTable createTable() {

        PdfPTable table = new PdfPTable(3);

        Stream.of("ID", "Name", "Salary")
                .forEach(col -> {
                    PdfPCell cell = new PdfPCell(new Phrase(col));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    table.addCell(cell);
                });

        table.addCell("1");
        table.addCell("Prasad");
        table.addCell("100000");

        table.addCell("2");
        table.addCell("Ravi");
        table.addCell("90000");

        return table;
    }
}
