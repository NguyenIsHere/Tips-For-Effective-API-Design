package com.example.demo.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.vn.zalopay.crypto.HMACUtil;
import java.util.Map;

@Service
public class ZaloPayOrderStatusService {

    private static final String ENDPOINT = "https://sb-openapi.zalopay.vn/v2/query";
    private static final String APP_ID = "2553";
    private static final String KEY1 = "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL";

    @SuppressWarnings("unchecked")
    public Map<String, Object> getOrderStatus(String orderRequestId) {
        // Prepare the data and MAC for the request
        String data = APP_ID + "|" + orderRequestId + "|" + KEY1;
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, KEY1, data);

        // Prepare headers and body
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("app_id", APP_ID);
        body.add("app_trans_id", orderRequestId);
        body.add("mac", mac);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Send the request using RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> response = restTemplate.exchange(ENDPOINT, HttpMethod.POST, request, Map.class);

        // Return the response body as a map
        return response.getBody();
    }
}
