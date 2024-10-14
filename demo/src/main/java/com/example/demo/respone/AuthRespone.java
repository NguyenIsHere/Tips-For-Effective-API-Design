package com.example.demo.respone;

import com.example.demo.model.USER_ROLE;

import lombok.Data;

@Data
public class AuthRespone {
    private String jwt;
    private String message;
    private USER_ROLE role;
}
