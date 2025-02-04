package com.example.billingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "factures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    @Id
    private String id;
    private String idCmd;
    private String idClient;
    private List<InvoiceItem> items;
    private Double orderAmount;
    private Double deliveryFee;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private String pdfUrl;
    private Boolean isPaid;
}

