package com.example.demo.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(
   collection = "products"
)
@Data
public class Product {
   @Id
   private String productId;
   private String name;
   private String description;
   private String category;
   private Double price;
   private List<Color> colors;

   public Product() {
   }

   public Product(String name, String description, String category, List<Color> colors, double price) {
      this.name = name;
      this.description = description;
      this.category = category;
      this.colors = colors;
      this.price = price;
   }
}
