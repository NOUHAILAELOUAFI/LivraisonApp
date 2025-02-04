package com.example.billingservice.service;

import com.example.billingservice.entity.Invoice;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Slf4j
public class JasperReportGenerator {

    @Value("${report.template.path}")
    private String templatePath;

    @Value("${report.output.path}")
    private String outputPath;


    private final ResourceLoader resourceLoader;
    @Autowired
    public JasperReportGenerator(@Qualifier("webApplicationContext") ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String generateInvoicePdf(Invoice invoice) {
        try {
            // Charger le template avec ResourceLoader
            Resource resource = resourceLoader.getResource(templatePath);
            JasperReport jasperReport = JasperCompileManager
                    .compileReport(resource.getInputStream());

            // Préparer les données
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("invoice", invoice);
            parameters.put("items", invoice.getItems());

            // Créer le dossier de sortie s'il n'existe pas
            File outputDir = new File(outputPath);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Générer le PDF
            JasperPrint jasperPrint = JasperFillManager
                    .fillReport(jasperReport, parameters, new JREmptyDataSource());

            // Sauvegarder le fichier
            String fileName = "invoice_" + invoice.getId() + ".pdf";
            String filePath = outputPath + File.separator + "pdf" + File.separator + fileName;

            // Créer le dossier pdf s'il n'existe pas
            new File(outputPath + File.separator + "pdf").mkdirs();

            JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);

            return fileName;
        } catch (Exception e) {
            log.error("Erreur lors de la génération du PDF", e);
            throw new RuntimeException("Erreur de génération de facture", e);
        }
    }
}