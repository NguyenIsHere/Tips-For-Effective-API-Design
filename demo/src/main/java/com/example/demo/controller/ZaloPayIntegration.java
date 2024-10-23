package com.example.demo.controller;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.example.demo.vn.zalopay.crypto.HMACUtil; // Đảm bảo bạn đã thêm thư viện HMACUtil

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ZaloPayIntegration {

    private static final Map<String, String> config = new HashMap<>() {{
        put("app_id", "2553");
        put("key1", "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL");
        put("key2", "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz");
        put("endpoint", "https://sb-openapi.zalopay.vn/v2/create");
    }};

    public static void main(String[] args) {
        // Khởi tạo dữ liệu đơn hàng
        String embedData = "{}"; // Dữ liệu của Merchant
        String items = "[]"; // Dữ liệu của Merchant
        Random rand = new Random();
        int transID = rand.nextInt(1000000); // ID giao dịch ngẫu nhiên

        // Tạo đối tượng đơn hàng
        Map<String, Object> order = new HashMap<>();
        order.put("app_id", config.get("app_id"));
        order.put("app_time", System.currentTimeMillis()); // milliseconds
        order.put("app_trans_id", String.format("%s_%d", getCurrentDate("yyMMdd"), transID)); // ID giao dịch
        order.put("app_user", "user123");
        order.put("item", items);
        order.put("embed_data", embedData);
        order.put("amount", 50000);
        order.put("description", "Lazada - Payment for the order #" + transID);
        order.put("bank_code", "");

        // Tạo MAC
        String data = String.join("|", order.get("app_id").toString(), order.get("app_trans_id").toString(),
                order.get("app_user").toString(), order.get("amount").toString(),
                order.get("app_time").toString(), order.get("embed_data").toString(),
                order.get("item").toString());

        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data));

        // Gửi yêu cầu POST tới ZaloPay
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        order.forEach(body::add);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String response = restTemplate.exchange(config.get("endpoint"), HttpMethod.POST, requestEntity, String.class).getBody();

        // Xử lý phản hồi
        if (response != null) {
            // In kết quả
            System.out.println("Response from ZaloPay:");
            System.out.println(response);
        } else {
            System.out.println("No response from ZaloPay.");
        }
    }

    // Phương thức lấy ngày hiện tại theo định dạng
    private static String getCurrentDate(String format) {
        return new java.text.SimpleDateFormat(format).format(new java.util.Date());
    }
}
