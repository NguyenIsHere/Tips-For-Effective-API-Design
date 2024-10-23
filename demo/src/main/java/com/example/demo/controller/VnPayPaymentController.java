package com.example.demo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.demo.config.VNPAYConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/vnpay")
public class VnPayPaymentController {

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestParam long amount,
                                           @RequestParam(required = false) String bankCode,
                                           @RequestParam(required = false) String language,
        HttpServletRequest req) throws IOException {

      String vnp_Version = "2.1.0";
      String vnp_Command = "pay";
      String orderType = "other";
      long vnp_Amount = amount * 100L;

      String vnp_TxnRef = VNPAYConfig.getRandomNumber(8);
      String vnp_IpAddr = VNPAYConfig.getIpAddress(req);

      String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;

      Map<String, String> vnp_Params = new HashMap<>();
      vnp_Params.put("vnp_Version", vnp_Version);
      vnp_Params.put("vnp_Command", vnp_Command);
      vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
      vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
      vnp_Params.put("vnp_CurrCode", "VND");

      if (bankCode != null && !bankCode.isEmpty()) {
        vnp_Params.put("vnp_BankCode", bankCode);
      }

      vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
      vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
      vnp_Params.put("vnp_OrderType", orderType);

      if (language != null && !language.isEmpty()) {
        vnp_Params.put("vnp_Locale", language);
      } else {
        vnp_Params.put("vnp_Locale", "vn");
      }

      vnp_Params.put("vnp_ReturnUrl", VNPAYConfig.vnp_ReturnUrl);
      vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

      Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
      String vnp_CreateDate = formatter.format(cld.getTime());
      vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

      cld.add(Calendar.MINUTE, 15);
      String vnp_ExpireDate = formatter.format(cld.getTime());
      vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

      List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
      Collections.sort(fieldNames);
      StringBuilder hashData = new StringBuilder();
      StringBuilder query = new StringBuilder();
      Iterator<String> itr = fieldNames.iterator();
      while (itr.hasNext()) {
        String fieldName = itr.next();
        String fieldValue = vnp_Params.get(fieldName);
        if ((fieldValue != null) && (fieldValue.length() > 0)) {
          hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
          query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=')
              .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
          if (itr.hasNext()) {
            query.append('&');
            hashData.append('&');
          }
        }
      }

      String vnp_SecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.secretKey, hashData.toString());
      query.append("&vnp_SecureHash=").append(vnp_SecureHash);
      String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + query.toString();

      JsonObject job = new JsonObject();
      job.addProperty("code", "00");
      job.addProperty("message", "success");
      job.addProperty("data", paymentUrl);

      return ResponseEntity.ok(new Gson().toJson(job));
    }
    
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<String> handleVnPayCallback(@RequestParam Map<String, String> allRequestParams) {
        // Lấy các tham số từ request
        String vnp_ResponseCode = allRequestParams.get("vnp_ResponseCode");
        String vnp_TxnRef = allRequestParams.get("vnp_TxnRef");
        String vnp_SecureHash = allRequestParams.get("vnp_SecureHash");
        
        // Thực hiện xác thực secure hash nếu cần
        // TODO: Thực hiện xác thực secure hash

        // Xử lý kết quả
        if ("00".equals(vnp_ResponseCode)) {
            // Giao dịch thành công
            return ResponseEntity.ok("Payment success for transaction reference: " + vnp_TxnRef);
        } else {
            // Giao dịch thất bại
            return ResponseEntity.ok("Payment failed for transaction reference: " + vnp_TxnRef);
        }
    }


}

