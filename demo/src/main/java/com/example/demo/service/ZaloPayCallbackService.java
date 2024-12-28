package com.example.demo.service;

import com.example.demo.repository.OrderRequestRepository;
import com.example.demo.vn.zalopay.crypto.HMACUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ZaloPayCallbackService {

    @Autowired
    private OrderRequestRepository orderRequestRepository;

    private static final String key2 = "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz"; // Khóa xác thực key2 được cung cấp bởi
                                                                           // ZaloPay

    public Map<String, Object> handleCallback(Map<String, Object> postdatajson) {
        Map<String, Object> result = new HashMap<>();

        try {
            // Lấy thông tin từ body của callback
            String data = (String) postdatajson.get("data");
            String requestMac = (String) postdatajson.get("mac");

            // Tạo MAC bằng cách hash HMAC SHA256 với key2
            String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, key2, data);

            // Kiểm tra tính hợp lệ của callback
            if (!mac.equals(requestMac)) {
                // Callback không hợp lệ
                result.put("return_code", -1);
                result.put("return_message", "mac not equal");
            } else {
                // Thanh toán thành công, bạn có thể cập nhật trạng thái đơn hàng tại đây
                @SuppressWarnings("unchecked")
                Map<String, Object> datajson = new ObjectMapper().readValue(data, Map.class);
                String appTransId = (String) datajson.get("app_trans_id");
                // Tìm request order theo Id và cập nhật trạng thái lên mongoDB
                orderRequestRepository.findById(appTransId).ifPresent(orderRequest -> {
                    orderRequest.setStatus("COMPLETED");
                    orderRequestRepository.save(orderRequest);
                });

                // Trả về thông báo thanh toán thành công
                result.put("return_code", 1);
                result.put("return_message", "success");
            }
        } catch (Exception e) {
            // Trường hợp có lỗi xảy ra
            result.put("return_code", 0); // ZaloPay sẽ callback lại (tối đa 3 lần)
            result.put("return_message", e.getMessage());
        }

        return result;
    }
}
