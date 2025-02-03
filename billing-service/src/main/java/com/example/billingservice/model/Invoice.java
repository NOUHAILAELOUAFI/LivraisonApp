package com.example.billingservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@Document(collection = "invoices")
public class Invoice {
    @Id
    private String id;
    private String deliveryId;
    private LocalDateTime creationDate;
    private BigDecimal deliveryFee;
    private BigDecimal totalAmount;
    private String status;
    private String pdfUrl;
}
