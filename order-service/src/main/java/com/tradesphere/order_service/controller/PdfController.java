package com.tradesphere.order_service.controller;

import com.tradesphere.order_service.model.Employee;
import com.tradesphere.order_service.service.pdf.HtmlPdfService;
import com.tradesphere.order_service.service.pdf.PdfGeneratorService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PdfGeneratorService pdfService;
    private final HtmlPdfService htmlPdfService;

    public PdfController(PdfGeneratorService pdfService, HtmlPdfService htmlPdfService) {
        this.pdfService = pdfService;
        this.htmlPdfService = htmlPdfService;
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadPdf() {

        byte[] pdf = pdfService.generatePdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


    @GetMapping(value = "/inlinepdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> inlinePdf() {
        byte[] pdf = pdfService.generatePdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=inline-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/html")
    public ResponseEntity<byte[]> htmlPdf() throws Exception {

        List<Employee> list = List.of(
                new Employee(1, "Prasad", 100000),
                new Employee(2, "Ravi", 90000)
        );

        byte[] pdf = htmlPdfService.generatePdf(list);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=employees.pdf")
                .body(pdf);
    }

}
