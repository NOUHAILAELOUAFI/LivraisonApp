package com.example.billingservice.client;

import com.example.billingservice.model.Commande;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "commande-service")
public interface CommandeServiceClient {
    @GetMapping("/commandes/{idCmd}")
    Commande getCommande(@PathVariable String idCmd);
}
