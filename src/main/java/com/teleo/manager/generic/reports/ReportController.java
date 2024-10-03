package com.teleo.manager.generic.reports;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import net.sf.jasperreports.engine.JRException;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RefreshScope
@RestController
@CrossOrigin("*")
@RequestMapping("/api/report")
@Tag(name = "Downloading report", description = "API de gestion des reports")
public class ReportController {

    private final ReportGenerator reportGenerator;

    public ReportController(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @GetMapping("/download/details/{reportFilePath}/{format}")
    @CrossOrigin(origins = {"*"}, exposedHeaders = {"Content-Disposition"})
    public void download(@PathVariable("reportFilePath") String reportFilePath, @PathVariable("format") String format, @NotEmpty @RequestParam(value = "params") Map<String, Object> params, HttpServletResponse response) throws URISyntaxException, JRException, IOException {

        //File contains all stored paths, names, and extensions
        Path fileNamePath = reportGenerator.downloadReportToPdf(reportFilePath, params, format);
        fileNamePath = fileNamePath.toAbsolutePath();

        if (!Files.exists(fileNamePath)) {
            return;
        }

        // Specifying a File
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Transfer-Encoding", "binary;");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileNamePath + "\"");
        try {
            Files.copy(fileNamePath, response.getOutputStream());
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }
}