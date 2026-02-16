package com.finance.controller;

import com.finance.service.PdfExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ExportController {

    private final PdfExportService pdfExportService;

    /**
     * Export a certified PDF financial report for a user.
     */
    @GetMapping("/pdf/{userId}")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Integer userId) {
        byte[] pdfBytes = pdfExportService.generateReport(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "finance-report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
