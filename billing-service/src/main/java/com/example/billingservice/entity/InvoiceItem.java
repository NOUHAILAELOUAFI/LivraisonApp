package com.example.billingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    private String idProduit;
    private String productName;
    private double price;
    private int quantity;
    private double subTotal;
}
