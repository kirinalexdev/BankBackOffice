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

        Map<String, String> params = new HashMap<>();
        params.put("pair", pair);
        params.put("APIKEY", APIKEY);

        RestTemplate restTemplate = new RestTemplate(); // TODO норм через new получать или лучше внедрением завимисостей? типа @Bean
        var response = restTemplate.getForEntity(url, String.class, params);
        var a = new ObjectMapper();

        System.out.println(response.getBody()); // TODO удалить, отладка
        System.out.println(response.getStatusCode()); // TODO удалить, отладка

        ObjectMapper mapper = new ObjectMapper(); // TODO норм так создавать или внедрением завимисомти нужно?
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // TODO как эскалировать ошибку наверх и нужно ли?
        // TODO ловить и логировать ошибки на вернхем уровне каждого слоя?
        // TODO логирвать ошибку response.getStatusCodeValue()  response.getStatusCode()

//        // TODO нужно так?
//        if (response.getStatusCode() != HttpStatus.OK) {
//            throw new RuntimeException(что тут?);
//        }

        // TODO спросить, почему в контроллере в monthlyTotals мне нужно в сигнатуру добавлять throw, при том  что есть @ErrorHandler
        int bodyStatus = rootNode.get("status").asInt();
        if (bodyStatus != 200) {
            String message = "Не удалось получить курс валюты. " + rootNode.get("message").asText();
            throw new CurrencyRateException(message, bodyStatus);
        }

        return rootNode.get("data").get(pair).asDouble();  // TODO завернуть в попытку?
    }

}
