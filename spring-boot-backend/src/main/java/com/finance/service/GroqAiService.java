package com.finance.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.dto.OcrResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GroqAiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.model}")
    private String model;

    public GroqAiService(@Value("${groq.api.url}") String apiUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Smart Scan OCR: Analyzes a receipt image using Groq's Llama Vision model.
     * Sends the image as base64 to the multimodal model which extracts
     * amount, date, category, and item details from the receipt.
     */
    public OcrResult analyzeReceipt(byte[] imageBytes, String mimeType) {
        try {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String imageUrl = "data:" + mimeType + ";base64," + base64Image;

            Map<String, Object> requestBody = Map.of(
                    "model", "meta-llama/llama-4-scout-17b-16e-instruct",
                    "messages", List.of(
                            Map.of("role", "system", "content",
                                    "You are a receipt/invoice OCR analysis expert. Extract financial information from receipt images. " +
                                    "Always respond with valid JSON only, no other text. Use this exact format:\n" +
                                    "{\"amount\": 0.00, \"category\": \"string\", \"itemName\": \"string\", " +
                                    "\"description\": \"string\", \"date\": \"yyyy-MM-dd HH:mm:ss\", " +
                                    "\"currency\": \"string\", \"confidence\": 0.0}\n" +
                                    "For category, choose from: Groceries, Transport, Entertainment, Utilities, " +
                                    "Restaurant, Shopping, Health, Education, Salary, Other.\n" +
                                    "If date is not visible, use today's date. Confidence is 0.0-1.0."),
                            Map.of("role", "user", "content", List.of(
                                    Map.of("type", "text", "text",
                                            "Analyze this receipt image. Extract the total amount, date, " +
                                            "store/item name, and suggest a category. Return JSON only."),
                                    Map.of("type", "image_url", "image_url", Map.of("url", imageUrl))
                            ))
                    ),
                    "temperature", 0.1,
                    "max_tokens", 500
            );

            String response = webClient.post()
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseOcrResponse(response);
        } catch (Exception e) {
            log.error("Error analyzing receipt with Groq AI: {}", e.getMessage(), e);
            return createDefaultOcrResult("Error analyzing receipt: " + e.getMessage());
        }
    }

    /**
     * Text-based receipt analysis for when image upload is not available.
     * Uses Llama 3.3 to extract financial data from pasted receipt text.
     */
    public OcrResult analyzeReceiptText(String receiptText) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content",
                                    "You are a receipt/invoice text analysis expert. Extract financial information from receipt text. " +
                                    "Always respond with valid JSON only, no other text. Use this exact format:\n" +
                                    "{\"amount\": 0.00, \"category\": \"string\", \"itemName\": \"string\", " +
                                    "\"description\": \"string\", \"date\": \"yyyy-MM-dd HH:mm:ss\", " +
                                    "\"currency\": \"string\", \"confidence\": 0.0}\n" +
                                    "For category, choose from: Groceries, Transport, Entertainment, Utilities, " +
                                    "Restaurant, Shopping, Health, Education, Salary, Other.\n" +
                                    "If date is not visible, use today's date. Confidence is 0.0-1.0."),
                            Map.of("role", "user", "content",
                                    "Analyze this receipt text and extract the total amount, date, store/item name, " +
                                    "and suggest a category. Return JSON only.\n\nReceipt text:\n" + receiptText)
                    ),
                    "temperature", 0.1,
                    "max_tokens", 500
            );

            String response = webClient.post()
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return parseOcrResponse(response);
        } catch (Exception e) {
            log.error("Error analyzing receipt text with Groq AI: {}", e.getMessage(), e);
            return createDefaultOcrResult("Error analyzing receipt text: " + e.getMessage());
        }
    }

    /**
     * Budget recommendation using AI
     */
    public String getBudgetRecommendation(String financialContext) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "system", "content",
                                    "You are a personal finance advisor for students and young professionals. " +
                                    "Give concise, actionable budget advice in 2-3 paragraphs. " +
                                    "Be specific with numbers and percentages. Be encouraging but honest."),
                            Map.of("role", "user", "content", financialContext)
                    ),
                    "temperature", 0.7,
                    "max_tokens", 500
            );

            String response = webClient.post()
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            log.error("Error getting budget recommendation: {}", e.getMessage(), e);
            return "Unable to generate recommendation at this time. Please check your spending trends in the dashboard.";
        }
    }

    private OcrResult parseOcrResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            // Clean up the content - remove markdown code blocks if present
            content = content.replaceAll("```json\\s*", "").replaceAll("```\\s*", "").trim();

            JsonNode data = objectMapper.readTree(content);

            OcrResult result = new OcrResult();
            result.setAmount(new BigDecimal(data.path("amount").asText("0")));
            result.setCategory(data.path("category").asText("Other"));
            result.setItemName(data.path("itemName").asText("Unknown"));
            result.setDescription(data.path("description").asText(""));
            result.setCurrency(data.path("currency").asText("TND"));
            result.setConfidence(data.path("confidence").asDouble(0.5));
            result.setRawText(content);

            String dateStr = data.path("date").asText("");
            if (!dateStr.isEmpty()) {
                try {
                    result.setDate(LocalDateTime.parse(dateStr,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } catch (Exception e) {
                    result.setDate(LocalDateTime.now());
                }
            } else {
                result.setDate(LocalDateTime.now());
            }

            return result;
        } catch (Exception e) {
            log.error("Error parsing OCR response: {}", e.getMessage(), e);
            return createDefaultOcrResult("Could not parse AI response");
        }
    }

    private OcrResult createDefaultOcrResult(String errorMessage) {
        OcrResult result = new OcrResult();
        result.setAmount(BigDecimal.ZERO);
        result.setCategory("Other");
        result.setItemName("Unknown");
        result.setDescription(errorMessage);
        result.setDate(LocalDateTime.now());
        result.setCurrency("TND");
        result.setConfidence(0.0);
        result.setRawText(errorMessage);
        return result;
    }
}
