package com.kirinalex.BankBackOffice.finance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.exceptions.CurrencyRateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Slf4j
@Component
public class Currency {

    @Value("${currate.url}")
    private String url;

    @Value("${currate.apikey}")
    private String apiKey;

    public double currencyRate(String baseCurrency) throws CurrencyRateException {
        final var QUOTED_CURRENCY = "RUB";
        final var PAIR = baseCurrency + QUOTED_CURRENCY;
        final var PREFIX_ERROR_MESSAGE = "Не удалось получить курс валюты. ";

        var params = new HashMap<>();
        params.put("pairs", PAIR);
        params.put("key", apiKey);

        var restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class, params);
        } catch (Exception ex) {
            log.error("{} {}. baseCurrency:{} responseBody:{}", PREFIX_ERROR_MESSAGE, ex.getMessage(), baseCurrency, ex);
            throw new CurrencyRateException(PREFIX_ERROR_MESSAGE, HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CurrencyRateException(PREFIX_ERROR_MESSAGE, response.getStatusCode());
        }

        var mapper = new ObjectMapper();
        JsonNode rootNode;
        var responseBody = response.getBody();
        try {
            rootNode = mapper.readTree(responseBody);
        } catch (JsonProcessingException ex) {
            log.error("{} {}. baseCurrency:{} responseBody:{}", PREFIX_ERROR_MESSAGE, ex.getMessage(), baseCurrency, responseBody, ex);
            throw new CurrencyRateException(PREFIX_ERROR_MESSAGE, HttpStatus.SERVICE_UNAVAILABLE);
        }

        var statusCode = rootNode.get("status").asInt();
        var status = HttpStatus.valueOf(statusCode);

        if (status != HttpStatus.OK) {
            String message = (PREFIX_ERROR_MESSAGE + rootNode.get("message").asText());
            log.error("{}. baseCurrency:{} statusCode{} responseBody:{}", message, baseCurrency, statusCode, responseBody, new Throwable());
            throw new CurrencyRateException(message, status);
        }

        try {
            return rootNode.get("data").get(PAIR).asDouble();
        } catch (Exception ex) {
            log.error("{}. baseCurrency:{} responseBody:{}", ex.getMessage(), baseCurrency, responseBody, ex);
            throw new CurrencyRateException(PREFIX_ERROR_MESSAGE, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
