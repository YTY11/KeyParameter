package com.fox.http;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author
 * @create 2019-12-06 18:01
 */
@RestController
public class HttpClient {

    public String client(String url, HttpMethod method, MultiValueMap<String, String> params) {

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> forEntity = template.getForEntity(url, String.class);
        return forEntity.getBody();
    }

}
