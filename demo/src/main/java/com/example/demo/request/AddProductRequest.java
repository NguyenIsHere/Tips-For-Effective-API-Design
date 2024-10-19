package com.example.demo.request;

import lombok.Data;

@Data
public class AddProductRequest {
    private String productId;
    private int quantity;
}
