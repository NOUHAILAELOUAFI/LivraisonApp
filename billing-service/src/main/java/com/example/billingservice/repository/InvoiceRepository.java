package com.example.billingservice.repository;

import com.example.billingservice.entity.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    List<Invoice> findByIdClient(String idClient);
    Optional<Invoice> findByIdCmd(String idCmd);
}
