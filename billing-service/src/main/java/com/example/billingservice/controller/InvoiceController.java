package com.example.billingservice.controller;


import com.example.billingservice.dto.InvoiceDTO;
import com.example.billingservice.dto.InvoiceItemDTO;
import com.example.billingservice.entity.Invoice;
import com.example.billingservice.entity.InvoiceItem;
import com.example.billingservice.service.InvoicePdfService;
import com.example.billingservice.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/factures")
public class InvoiceController {
    @Autowired
    private InvoicePdfService invoicePdfService;

    @GetMapping(value = "/generate/{idCmd}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> testPdfGeneration(@PathVariable String idCmd) {
        try {
            // Create a test invoice
            Invoice invoice = new Invoice();
            invoice = invoiceService.createInvoice(idCmd);
            log.info(invoice.toString());

            String pdfFilePath = invoicePdfService.generateInvoicePdf(invoice);

            // Read PDF file into bytes
            byte[] pdfBytes = Files.readAllBytes(Paths.get(pdfFilePath));

            // Return PDF with proper headers
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                    .body(pdfBytes);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/client/{idClient}")
    public ResponseEntity<List<InvoiceDTO>> getClientInvoices(@PathVariable String idClient) {
        List<Invoice> invoices = invoiceService.getClientInvoices(idClient);
        List<InvoiceDTO> dtos = invoices.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }



    private InvoiceDTO convertToDTO(Invoice invoice) {
        return InvoiceDTO.builder()
                .id(invoice.getId())
                .idCmd(invoice.getIdCmd())
                .idClient(invoice.getIdClient())
                .items(invoice.getItems().stream()
                        .map(this::convertItemToDTO)
                        .collect(Collectors.toList()))
                .orderAmount(invoice.getOrderAmount())
                .deliveryFee(invoice.getDeliveryFee())
                .totalAmount(invoice.getTotalAmount())
                .createdAt(invoice.getCreatedAt())
                .isPaid(invoice.getIsPaid())
                .build();
    }

    private InvoiceItemDTO convertItemToDTO(InvoiceItem item) {
        return InvoiceItemDTO.builder()
                .idProduit(item.getIdProduit())
                .productName(item.getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .subTotal(item.getSubTotal())
                .build();
    }
}