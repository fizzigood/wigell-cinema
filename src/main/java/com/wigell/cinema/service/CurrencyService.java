package com.wigell.cinema.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

// Konverterar SEK → USD via currency-converter. Fallback-kurs om tjänsten är nere.
@Service
public class CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyService.class);

    // Fast fallback-kurs om mikrotjänsten är nere eller inte svarar.
    private static final double FALLBACK_USD_RATE = 0.091;

    private final RestTemplate restTemplate;
    private final String currencyServiceUrl;

    public CurrencyService(RestTemplate restTemplate,
                           @Value("${currency-service.url:http://localhost:8586}") String currencyServiceUrl) {
        this.restTemplate = restTemplate;
        this.currencyServiceUrl = currencyServiceUrl;
    }

    // Fallback vid fel
    @SuppressWarnings("unchecked")
    public double sekToUsd(double sek) {
        try {
            String url = currencyServiceUrl + "/api/v1/currency/convert?amount=" + sek + "&from=SEK&to=USD";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null && response.containsKey("to")) {
                Map<String, Object> toMap = (Map<String, Object>) response.get("to");
                double converted = ((Number) toMap.get("amount")).doubleValue();
                return Math.round(converted * 100.0) / 100.0;
            }
        } catch (Exception e) {
            logger.warn("Valutakonverteringsmikrotjänsten otillgänglig, använder reservkurs. Fel: {}", e.getMessage());
        }
        // Fallback: fast kurs om mikrotjänsten inte svarar
        return Math.round(sek * FALLBACK_USD_RATE * 100.0) / 100.0;

    }

}