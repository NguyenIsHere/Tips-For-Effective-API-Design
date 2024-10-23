package com.example.demo.model;

import lombok.Data;

@Data
public class OrderRequest {
  private int amount;
  private String appUser;
  private String description;
  private String embedData;
  private String callbackUrl;
  private String item;
  private String bankCode;
}

