package com.example.billingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Produit {
    private String idProduit;
    private String ProductName;
    private Double price;
    private int quantity;
}
