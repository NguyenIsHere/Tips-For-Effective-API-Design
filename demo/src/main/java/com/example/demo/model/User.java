package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
@Document(
   collection = "users"
)
public class User {
    @Id
    private String id;
    private String email;
    private String phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String fullname;
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
    private Address addresses;
}
