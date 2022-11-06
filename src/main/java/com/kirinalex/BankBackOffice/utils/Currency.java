package com.kirinalex.BankBackOffice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class Currency {

    public static double currencyRate(String baseCurrency) throws CurrencyRateException {
        String url = "https://currate.ru/api/?get=rates&pairs={pair}&key={APIKEY}";
        String APIKEY = "b55fd8a9dbf46c8c7f1d959e1635f03c"; // TODO вынести куда то в настройки? или как хранить?
        String quotedCurrency = "RUB";
        String pair = baseCurrency + quotedCurrency;
        String prefixErrorMessage = "Не удалось получить курс валюты. "; // TODO подобные вещи как то через лямбду реализуются?

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);
        params.put("APIKEY", APIKEY);

        RestTemplate restTemplate = new RestTemplate(); // TODO норм так создавать или внедрением завимисомти нужно?
        var response = restTemplate.getForEntity(url, String.class, params);// TODO завернуть в попытку и логировать детали вызова, или достаточная инфа будет в ExceptionsHandler?

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CurrencyRateException(prefixErrorMessage, response.getStatusCode());
        }

        // TODO отладка
        // TODO логирвать ошибки
        System.out.println(response.getBody());
        System.out.println(response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper(); // TODO норм так создавать или внедрением завимисомти нужно?
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // TODO это чем будет обрабатываться?
        }


        //int r = 1/0;

        HttpStatus statusCode = HttpStatus.valueOf(rootNode.get("status").asInt());

        if (statusCode != HttpStatus.OK) {
            String message = (prefixErrorMessage + rootNode.get("message").asText());
            throw new CurrencyRateException(message, statusCode);
        }

        return rootNode.get("data").get(pair).asDouble(); // TODO завернуть в попытку и логировать детали парсинга?
    }

}
