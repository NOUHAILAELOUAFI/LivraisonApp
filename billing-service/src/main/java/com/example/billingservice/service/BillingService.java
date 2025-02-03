package com.example.billingservice.service;

import com.example.billingservice.model.Invoice;
import com.example.billingservice.repository.InvoiceRepository;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class BillingService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public BigDecimal computeDeliveryFee(double distance, double weight) {
        // Logique de calcul des frais de livraison
        BigDecimal baseFee = new BigDecimal("5.00");
        BigDecimal distanceFee = new BigDecimal(distance * 0.5);
        BigDecimal weightFee = new BigDecimal(weight * 0.2);

        return baseFee.add(distanceFee).add(weightFee);
    }

    public Invoice generateInvoice(String deliveryId, double distance, double weight) {
        Invoice invoice = new Invoice();
        invoice.setDeliveryId(deliveryId);
        invoice.setCreationDate(LocalDateTime.now());
        invoice.setDeliveryFee(computeDeliveryFee(distance, weight));
        invoice.setTotalAmount(invoice.getDeliveryFee());
        invoice.setStatus("CREATED");

        return invoiceRepository.save(invoice);
    }

    public byte[] generatePdfInvoice(String invoiceId) throws JRException {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        // Préparation des données pour le rapport
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("invoiceId", invoice.getId());
        parameters.put("deliveryId", invoice.getDeliveryId());
        parameters.put("amount", invoice.getTotalAmount());
        parameters.put("date", invoice.getCreationDate());

        // Génération du PDF avec JasperReports
        JasperReport jasperReport = JasperCompileManager
                .compileReport(getClass().getResourceAsStream("/templates/invoice_template.jrxml"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}