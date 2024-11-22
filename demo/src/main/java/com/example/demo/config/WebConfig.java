// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
   public WebConfig() {
   }

   public void addCorsMappings(CorsRegistry registry) {
      registry.addMapping("/**").allowedOrigins(new String[] { "http://localhost:3000" })
            .allowedMethods(new String[] { "GET", "POST", "PUT", "DELETE" }).allowedHeaders(new String[] { "*" })
            .allowCredentials(true);
   }
}
