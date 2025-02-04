package com.example.billingservice.client;

import com.example.billingservice.model.Produit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produit-service")
public interface ProduitServiceClient {
    @GetMapping("/produits/{id}")
    Produit getProduit(@PathVariable String id);
}
