package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.model.OrderRequest;
import com.example.demo.vn.zalopay.crypto.HMACUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ZaloPayCreateOrderService {

    private static final Map<String, String> config = new HashMap<>() {{
        put("app_id", "2553");
        put("key1", "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL");
        put("key2", "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz");
        put("endpoint", "https://sb-openapi.zalopay.vn/v2/create");
    }};

    // Phương thức tạo đơn hàng dựa trên dữ liệu từ OrderRequest
    public String createOrder(OrderRequest orderRequest) {
        // Tạo ID giao dịch ngẫu nhiên
        Random rand = new Random();
        int transID = rand.nextInt(1000000);

        // Chuẩn bị dữ liệu đơn hàng
        Map<String, Object> order = new HashMap<>();
        order.put("app_id", config.get("app_id"));
        order.put("app_time", System.currentTimeMillis());
        order.put("app_trans_id", String.format("%s_%d", getCurrentDate("yyMMdd"), transID));
        order.put("app_user", orderRequest.getAppUser());
        order.put("item", orderRequest.getItem());
        order.put("embed_data", orderRequest.getEmbedData());
        order.put("callback_url", orderRequest.getCallbackUrl()); // Thêm callback URL
        order.put("amount", orderRequest.getAmount());
        order.put("description", orderRequest.getDescription());
        order.put("bank_code", orderRequest.getBankCode());

        // Tạo MAC (chữ ký)
        String data = String.join("|",
                order.get("app_id").toString(),
                order.get("app_trans_id").toString(),
                order.get("app_user").toString(),
                order.get("amount").toString(),
                order.get("app_time").toString(),
                order.get("embed_data").toString(),
                order.get("item").toString()
        );
        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data));

        // Gửi yêu cầu POST tới ZaloPay
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        order.forEach(body::add);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String response = restTemplate.exchange(config.get("endpoint"), HttpMethod.POST, requestEntity, String.class).getBody();

        // Trả về phản hồi từ ZaloPay
        return response != null ? response : "No response from ZaloPay.";
    }

    // Phương thức lấy ngày hiện tại theo định dạng
    private static String getCurrentDate(String format) {
        return new java.text.SimpleDateFormat(format).format(new java.util.Date());
    }
}
