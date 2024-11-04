package com.example.demo.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class MongoConfig {
    public MongoClient createMongoClient() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToConnectionPoolSettings(builder -> 
                        builder.maxSize(50) // Số kết nối tối đa
                               .minSize(10) // Số kết nối tối thiểu
                               .maxWaitTime(1000, TimeUnit.MILLISECONDS)) // Thời gian chờ
                .applyToClusterSettings(builder -> 
                        builder.hosts(Collections.singletonList(new ServerAddress("localhost", 27017))))
                .build();
        
        return MongoClients.create(settings);
    }
}

