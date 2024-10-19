package com.example.demo.model;

import lombok.Data;
@Data
public class Address {
    private String streetAddress;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
}
