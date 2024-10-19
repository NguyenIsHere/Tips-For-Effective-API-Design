package com.example.demo.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(
   collection = "products"
)
public class Product {
   @Id
   private String productId;
   private String name;
   private String description;
   private Long price;
   private String category;
   private List<Color> colors;

   public Product() {
   }

   public Product(String name, String description, Long price, String category, List<Color> colors) {
      this.name = name;
      this.description = description;
      this.price = price;
      this.category = category;
      this.colors = colors;
   }

   public String getProductId() {
      return this.productId;
   }

   public void setProductId(String productId) {
      this.productId = productId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Long getPrice() {
      return this.price;
   }

   public void setPrice(Long price) {
      this.price = price;
   }

   public String getCategory() {
      return this.category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public List<Color> getColors() {
      return this.colors;
   }

   public void setColors(List<Color> colors) {
      this.colors = colors;
   }
}
