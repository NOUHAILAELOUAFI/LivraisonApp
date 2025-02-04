package com.example.billingservice.service;

import com.example.billingservice.client.CommandeServiceClient;
import com.example.billingservice.client.ProduitServiceClient;
import com.example.billingservice.entity.Invoice;
import com.example.billingservice.entity.InvoiceItem;
import com.example.billingservice.model.Commande;
import com.example.billingservice.model.CommandeItem;
import com.example.billingservice.model.Produit;
import com.example.billingservice.repository.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CommandeServiceClient commandeServiceClient;

    @Autowired
    private ProduitServiceClient produitServiceClient;


    public Invoice createInvoice(String idCmd) {
        try {
            // Récupérer la commande
            Commande commande = commandeServiceClient.getCommande(idCmd);

            // Récupérer les détails des produits et calculer les montants
            List<InvoiceItem> invoiceItems = new ArrayList<>();
            double orderAmount = 0.0;

            for (CommandeItem item : commande.getItems()) {
                Produit produit = produitServiceClient.getProduit(item.getIdProduit());
                double subTotal = produit.getPrice() * item.getQuantity();

                invoiceItems.add(InvoiceItem.builder()
                        .idProduit(produit.getIdProduit())
                        .productName(produit.getProductName())
                        .price(produit.getPrice())
                        .quantity(item.getQuantity())
                        .subTotal(subTotal)
                        .build());

                orderAmount += subTotal;
            }

            // Calculer les frais de livraison
            double deliveryFee = calculateDeliveryFee(commande);

            // Créer la facture
            Invoice invoice = Invoice.builder()
                    .idCmd(idCmd)
                    .idClient(commande.getIdClient())
                    .items(invoiceItems)
                    .orderAmount(orderAmount)
                    .deliveryFee(deliveryFee)
                    .totalAmount(Double.valueOf(orderAmount + deliveryFee))
                    .createdAt(LocalDateTime.now())
                    .isPaid(Boolean.FALSE)
                    .build();

            invoiceRepository.save(invoice);
            return invoice;

        } catch (Exception e) {
            log.error("Erreur lors de la création de la facture pour la commande: " + idCmd, e);
            throw new RuntimeException("Erreur lors de la création de la facture", e);
        }
    }

    private double calculateDeliveryFee(Commande commande) {
        // Frais fixes : base (2.99) + supplément livraison (0.5)
        return 3.49;
    }

    public List<Invoice> getClientInvoices(String idClient) {
        return invoiceRepository.findByIdClient(idClient);
    }

    public Invoice getInvoice(String id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée"));
    }
}
