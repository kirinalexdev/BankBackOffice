package com.kirinalex.BankBackOffice.finance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.utils.CurrencyRateException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Currency {

    public static double currencyRate(String baseCurrency) throws CurrencyRateException {

        var url = "https://currate.ru/api/?get=rates&pairs={pair}&key={APIKEY}";
        var APIKEY = "b55fd8a9dbf46c8c7f1d959e1635f03c";
        var quotedCurrency = "RUB";
        var pair = baseCurrency + quotedCurrency;
        var prefixErrorMessage = "Не удалось получить курс валюты. ";

        var params = new HashMap<>();
        params.put("pair", pair);
        params.put("APIKEY", APIKEY);

        var restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class, params);
        } catch (Exception ex) {
            log.error("{}. baseCurrency:{} responseBody:{}", ex.getMessage(), baseCurrency, ex);
            throw new CurrencyRateException(prefixErrorMessage, HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CurrencyRateException(prefixErrorMessage, response.getStatusCode());
        }

        var mapper = new ObjectMapper();
        JsonNode rootNode;
        var responseBody = response.getBody();
        try {
            rootNode = mapper.readTree(responseBody);
        } catch (JsonProcessingException ex) {
            log.error("{}. baseCurrency:{} responseBody:{}", ex.getMessage(), baseCurrency, responseBody, ex);
            throw new CurrencyRateException(prefixErrorMessage, HttpStatus.SERVICE_UNAVAILABLE);
        }

        var statusCode = rootNode.get("status").asInt();
        var status = HttpStatus.valueOf(statusCode);

        if (status != HttpStatus.OK) {
            String message = (prefixErrorMessage + rootNode.get("message").asText());
            log.error("{}. baseCurrency:{} statusCode{} responseBody:{}", message, baseCurrency, statusCode, responseBody, new Throwable());
            throw new CurrencyRateException(message, status);
        }

        try {
            return rootNode.get("data").get(pair).asDouble();
        } catch (Exception ex) {
            log.error("{}. baseCurrency:{} responseBody:{}", ex.getMessage(), baseCurrency, responseBody, ex);
            throw new CurrencyRateException(prefixErrorMessage, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
