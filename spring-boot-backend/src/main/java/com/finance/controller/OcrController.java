package com.finance.controller;

import com.finance.dto.OcrResult;
import com.finance.service.GroqAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OcrController {

    private final GroqAiService groqAiService;

    /**
     * Smart Scan: Upload a receipt image for AI-powered OCR analysis.
     * Supports JPEG, PNG, WebP image formats.
     * Returns extracted amount, category, date, and item details.
     */
    @PostMapping(value = "/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OcrResult> scanReceipt(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().build();
            }

            OcrResult result = groqAiService.analyzeReceipt(
                    file.getBytes(), contentType);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Text-based receipt analysis: paste receipt text for AI extraction.
     */
    @PostMapping("/scan-text")
    public ResponseEntity<OcrResult> scanReceiptText(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        if (text == null || text.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        OcrResult result = groqAiService.analyzeReceiptText(text);
        return ResponseEntity.ok(result);
    }
}
