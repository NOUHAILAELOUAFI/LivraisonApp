package com.example.billingservice.repository;

import com.example.billingservice.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
}
