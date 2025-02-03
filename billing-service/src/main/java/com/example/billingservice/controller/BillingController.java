package com.example.billingservice.controller;

import com.example.billingservice.model.Invoice;
import com.example.billingservice.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping("/invoices")
    public ResponseEntity<Invoice> generateInvoice(
            @RequestParam String deliveryId,
            @RequestParam double distance,
            @RequestParam double weight) {
        Invoice invoice = billingService.generateInvoice(deliveryId, distance, weight);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/invoices/{invoiceId}/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable String invoiceId) {
        try {
            byte[] pdfContent = billingService.generatePdfInvoice(invoiceId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("invoice-" + invoiceId + ".pdf").build());

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
