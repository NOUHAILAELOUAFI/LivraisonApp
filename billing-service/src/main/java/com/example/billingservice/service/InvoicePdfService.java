package com.example.billingservice.service;

import com.example.billingservice.entity.Invoice;
import com.example.billingservice.utils.JasperReportUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class InvoicePdfService {

    private static final String INVOICE_TEMPLATE = "invoice_template.jrxml";

    private static final String PDF_OUTPUT_DIR = "/app/invoices/";

    public String generateInvoicePdf(Invoice invoice) throws JRException, IOException {
        // Compile the Jasper report template
        JasperReport jasperReport = JasperReportUtils.compileReport(INVOICE_TEMPLATE);

        // Prepare parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("invoiceId", invoice.getId());
        parameters.put("clientId", invoice.getIdClient());
        parameters.put("orderAmount", invoice.getOrderAmount());
        parameters.put("deliveryFee", invoice.getDeliveryFee());
        parameters.put("totalAmount", invoice.getTotalAmount());
        parameters.put("createdAt", invoice.getCreatedAt());

        // Create datasource from invoice items
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(invoice.getItems());

        // Fill the report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Generate unique filename
        String filename = generateUniqueFileName(invoice.getId());

        // Export to PDF
        JasperExportManager.exportReportToPdfFile(jasperPrint, filename);

        return filename;
    }

    private String generateUniqueFileName(String invoiceId) {
        // Create directory if it doesn't exist
        File directory = new File(PDF_OUTPUT_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate a unique filename
        return PDF_OUTPUT_DIR + "invoice_" + invoiceId + "_" +
                UUID.randomUUID().toString() + ".pdf";
    }
}

