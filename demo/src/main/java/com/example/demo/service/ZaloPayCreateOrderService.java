package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.demo.model.Cart;
import com.example.demo.model.OrderRequest;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRequestRepository;
import com.example.demo.vn.zalopay.crypto.HMACUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ZaloPayCreateOrderService {

    @Autowired
    private OrderRequestRepository orderRequestRepository;

    @Autowired
    private UserService userService; // Để lấy thông tin người dùng từ JWT
    
    @Autowired
    private CartService cartService; // Để lấy thông tin giỏ hàng từ JWT

    private final ObjectMapper objectMapper;

    public ZaloPayCreateOrderService() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Đăng ký JavaTimeModule
    }
    
    private static final Map<String, String> config = new HashMap<>() {{
        put("app_id", "2553");
        put("key1", "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL");
        put("key2", "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz");
        put("endpoint", "https://sb-openapi.zalopay.vn/v2/create");
    }};

    // Phương thức tạo đơn hàng dựa trên dữ liệu từ OrderRequest
    public String createOrder(String jwt, OrderRequest orderRequest) throws Exception {
        // Tạo ID giao dịch ngẫu nhiên
        Random rand = new Random();
        int transID = rand.nextInt(1000000);

        User user = userService.findUserByJwtToken(jwt); // Lấy thông tin người dùng từ JWT
        orderRequest.setUserId(user.getId()); // Lưu ID người dùng vào đơn hàng
        Cart cart = cartService.getCartByJwtToken(jwt); // Lấy giỏ hàng của người dùng
        if (cart == null) {
            throw new Exception("Cart not found");
        }

        // Lấy danh sách CartItem từ giỏ hàng
        var cartItems = cart.getItems();

        // Chuyển đổi danh sách CartItem thành chuỗi JSON
        String itemsJson = objectMapper.writeValueAsString(cartItems);

        // Lưu danh sách sản phẩm vào đơn hàng
        orderRequest.setItem(itemsJson);
        
        // Tính tổng giá trị đơn hàng
        cart.calculateTotalPrice();
        double totalAmount = (Math.round(cart.getTotalPrice() * 100) / 100.0)*1000;

        // Chuyển đổi sang int (làm tròn) và lưu tổng giá trị vào đơn hàng
        orderRequest.setAmount((int) totalAmount);

        // Lấy thời gian hiện tại chuyển thành json string và gán vào embedData
        LocalDateTime now = LocalDateTime.now();
                // Định dạng LocalDateTime thành chuỗi
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // Giả lập JSON String
        String jsonDateTime = "{ \"dateTime\": \"" + formattedDateTime + "\" }";
        orderRequest.setEmbedData(jsonDateTime);

        // Chuẩn bị dữ liệu đơn hàng
        Map<String, Object> order = new HashMap<>();
        order.put("app_id", config.get("app_id"));
        order.put("app_time", System.currentTimeMillis());
        order.put("app_trans_id", String.format("%s_%d", getCurrentDate("yyMMdd"), transID));
        order.put("app_user", user.getId());
        order.put("item", orderRequest.getItem());
        order.put("embed_data", orderRequest.getEmbedData());
        order.put("callback_url", orderRequest.getCallbackUrl()); // Thêm callback URL
        order.put("amount", orderRequest.getAmount()); // Tổng giá trị đơn hàng
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

        // Lưu appTransId vào đơn hàng
        orderRequest.setOrderRequestId(order.get("app_trans_id").toString()); 

        // Lưu thông tin đơn hàng vào cơ sở dữ liệu
        orderRequestRepository.save(orderRequest);

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
