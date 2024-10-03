package com.teleo.manager.generic.reports;

import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.generic.utils.AppConstants;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportGenerator {
    public Path downloadReportToPdf(String reportFilePath, Map<String, Object> params, String format) throws JRException, IOException {
        // charge le fichier et compile-le
        params.put("titre", AppConstants.COMPANY_NAME);

        ClassPathResource file = new ClassPathResource(reportFilePath, this.getClass().getClassLoader());
        JasperPrint print = JasperFillManager.fillReport(JasperCompileManager.compileReport(file.getPath()), params, new JREmptyDataSource());

        // créer une méthode privée qui renvoie le lien vers le fichier pdf spécifique
        return getReportPath(print, format);
    }

    public Path downloadReportToPdf(String reportFilePath, List<? extends BaseEntity> source, String format) throws JRException, IOException {
        // Add parameters
        Map<String, Object> params = new HashMap<>();
        params.put("titre", AppConstants.COMPANY_NAME);

        ClassPathResource file = new ClassPathResource(reportFilePath, this.getClass().getClassLoader());
        JasperPrint print = JasperFillManager.fillReport(JasperCompileManager.compileReport(file.getPath()), params, new JRBeanCollectionDataSource(source));

        // créer une méthode privée qui renvoie le lien vers le fichier pdf spécifique
        return getReportPath(print, format);
    }

    private Path getReportPath(JasperPrint jasperPrint, String format) throws IOException, JRException {
        Path tempFile = null;
        String link = null;
        String uploadDir = StringUtils.cleanPath("file:/data/uploads/");
        Path uploadPath = Paths.get(uploadDir);
        // crée un dossier pour stocker le rapport
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        switch (format) {
            case "csv":
                tempFile = Files.createTempFile(uploadPath, null, ".csv");
                link = StringUtils.cleanPath(tempFile.toAbsolutePath().toString());
                uploadPath = Paths.get(link);
                csv(jasperPrint, link);
                break;
            case "xlsx":
                tempFile = Files.createTempFile(uploadPath, null, ".xlsx");
                link = StringUtils.cleanPath(tempFile.toAbsolutePath().toString());
                uploadPath = Paths.get(link);
                xlsx(jasperPrint, link);
                break;
            case "xml":
                tempFile = Files.createTempFile(uploadPath, null, ".xml");
                link = StringUtils.cleanPath(tempFile.toAbsolutePath().toString());
                uploadPath = Paths.get(link);
                // générer le rapport et l'enregistrer dans le dossier qui vient d'être créé
                JasperExportManager.exportReportToXmlFile(jasperPrint, link, true);
                break;
            case "html":
                tempFile = Files.createTempFile(uploadPath, null, ".html");
                link = StringUtils.cleanPath(tempFile.toAbsolutePath().toString());
                uploadPath = Paths.get(link);
                // générer le rapport et l'enregistrer dans le dossier qui vient d'être créé
                JasperExportManager.exportReportToHtmlFile(jasperPrint, link);
                break;
            default:
                tempFile = Files.createTempFile(uploadPath, null, ".pdf");
                link = StringUtils.cleanPath(tempFile.toAbsolutePath().toString());
                uploadPath = Paths.get(link);
                // générer le rapport et l'enregistrer dans le dossier qui vient d'être créé
                JasperExportManager.exportReportToPdfFile(jasperPrint, link);
                break;
        }
        return uploadPath;
    }

    private void csv(JasperPrint jasperPrint, String reportPath) throws JRException {
        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleWriterExporterOutput(reportPath));
        SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();
        configuration.setFieldDelimiter(",");
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }

    private void xlsx(JasperPrint jasperPrint, String reportPath) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath));
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setRemoveEmptySpaceBetweenColumns(true);
        configuration.setDetectCellType(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }
}
