package com.kirinalex.BankBackOffice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class Currency {

    // TODO сделать статическим?

    public static double currencyRate(String baseCurrency) {
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


        ObjectMapper mapper = new ObjectMapper(); // TODO норм так создавать или внедрением завимисомти нужно?
        JsonNode obj = null;
        try {
            obj = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return obj.get("data").get(pair).asDouble();

//        System.out.println(); // TODO как эскалировать ошибку наверх?
//        System.out.println(); // TODO логирвать ошибку response.getStatusCodeValue()  response.getStatusCode()

    }

}
