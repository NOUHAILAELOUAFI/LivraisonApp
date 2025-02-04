package com.example.billingservice.controller;


import com.example.billingservice.dto.InvoiceDTO;
import com.example.billingservice.dto.InvoiceItemDTO;
import com.example.billingservice.entity.Invoice;
import com.example.billingservice.entity.InvoiceItem;
import com.example.billingservice.service.InvoiceService;
import com.example.billingservice.service.JasperReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/factures")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    private JasperReportGenerator reportGenerator;

    @PostMapping("/generate/{idCmd}")
    public ResponseEntity<InvoiceDTO> generateInvoice(@PathVariable String idCmd) {
        Invoice invoice = invoiceService.createInvoice(idCmd);
        return ResponseEntity.ok(convertToDTO(invoice));
    }
    @GetMapping("/client/{idClient}")
    public ResponseEntity<List<InvoiceDTO>> getClientInvoices(@PathVariable String idClient) {
        List<Invoice> invoices = invoiceService.getClientInvoices(idClient);
        List<InvoiceDTO> dtos = invoices.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> generateAndGetInvoicePdf(@PathVariable String id) {
        try {
            Invoice invoice = invoiceService.getInvoice(id);
            String fileName = reportGenerator.generateInvoicePdf(invoice);
            Path path = Paths.get("src/main/resources/static/pdf/" + fileName);
            Resource resource = new FileSystemResource(path);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
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