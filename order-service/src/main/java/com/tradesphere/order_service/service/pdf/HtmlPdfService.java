package com.tradesphere.order_service.service.pdf;

import com.tradesphere.order_service.model.Employee;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.List;
import org.xhtmlrenderer.pdf.ITextRenderer;


@Service
public class HtmlPdfService {


    private final TemplateEngine templateEngine;

    public HtmlPdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(List<Employee> employees)  {

        Context context = new Context();
        context.setVariable("employees", employees);

        String html = templateEngine.process("report", context);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(out);

        return out.toByteArray();
    }
}