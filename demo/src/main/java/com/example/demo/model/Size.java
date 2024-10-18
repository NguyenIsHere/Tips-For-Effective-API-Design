// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.model;

import lombok.Data;

@Data
public class Size {
   private String sizeName;
   private int stockQuantity;
   private double price;

   public Size() {
   }

   public Size(String sizeName, int stockQuantity, double price) {
      this.sizeName = sizeName;
      this.stockQuantity = stockQuantity;
      this.price = price;
   }

   public String toString() {
      return "Size{sizeName='" + this.sizeName + "', stockQuantity=" + this.stockQuantity + ", price=" + this.price + "}";
   }
}
