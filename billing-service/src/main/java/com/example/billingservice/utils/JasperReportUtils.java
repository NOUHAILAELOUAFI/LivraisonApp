package com.example.billingservice.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class JasperReportUtils {

    public static JasperReport compileReport(String templatePath) throws JRException, IOException {
        try (InputStream inputStream = new ClassPathResource(templatePath).getInputStream()) {
            return JasperCompileManager.compileReport(inputStream);
        }
    }
}