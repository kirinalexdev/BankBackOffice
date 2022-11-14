package com.kirinalex.BankBackOffice.finance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.utils.CurrencyRateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class Currency {

    public static double currencyRate(String baseCurrency) throws CurrencyRateException {

        String url = "https://currate.ru/api/?get=rates&pairs={pair}&key={APIKEY}";
        String APIKEY = "b55fd8a9dbf46c8c7f1d959e1635f03c"; // TODO вынести куда то в настройки? или как хранить?
        String quotedCurrency = "RUB";
        String pair = baseCurrency + quotedCurrency;
        String prefixErrorMessage = "Не удалось получить курс валюты. ";

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);
        params.put("APIKEY", APIKEY);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class, params);
        } catch (Exception ex) {
            // TODO здесь логировать фактический текст ошибки урлом и параметрами вызова
            throw new CurrencyRateException(prefixErrorMessage, HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CurrencyRateException(prefixErrorMessage, response.getStatusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        var responseBody = response.getBody();
        try {
            rootNode = mapper.readTree(responseBody);
        } catch (JsonProcessingException ex) {
            // TODO здесь логировать ex.getMessage() и responseBody
            throw new CurrencyRateException(prefixErrorMessage, HttpStatus.SERVICE_UNAVAILABLE);
        }

        HttpStatus statusCode = HttpStatus.valueOf(rootNode.get("status").asInt());

        if (statusCode != HttpStatus.OK) {
            String message = (prefixErrorMessage + rootNode.get("message").asText());
            throw new CurrencyRateException(message, statusCode);
        }

        var dataNodeName = "data";
        try {
            return rootNode.get(dataNodeName).get(pair).asDouble();
        } catch (Exception ex) {
            // TODO здесь логировать ex.getMessage() и responseBody и "Не удалось как число Double получить данные узла json "+ dataNodeName + "." + pair;
            throw new CurrencyRateException(prefixErrorMessage, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
