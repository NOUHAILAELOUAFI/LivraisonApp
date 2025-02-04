package com.example.billingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private String id;
    private String idCmd;
    private String idClient;
    private List<InvoiceItemDTO> items;
    private Double orderAmount;
    private Double deliveryFee;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private Boolean isPaid;
}
