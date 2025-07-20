package com.example.langchain4j11.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private static final String API_KEY="713c8d766bc74e259ef283299f7a19fe";
    private static final String BASE_URL = "https://devapi.qweather.com/v7/weather/now?location=%s&key=%s";

    public JsonNode getWeatherV2(String city) throws Exception {
        String url = String.format(BASE_URL,city,API_KEY);
        var httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        String response = new RestTemplate(factory).getForObject(url,String.class);
        JsonNode jsonNode = new ObjectMapper().readTree(response);
        return jsonNode;
    }
}
