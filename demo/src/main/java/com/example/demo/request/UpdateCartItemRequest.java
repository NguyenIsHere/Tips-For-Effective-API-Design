package com.example.demo.request;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private String cartItemId;
    private int quantity;
}
