// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.model;

public class Size {
   private String sizeName;
   private int stockQuantity;

   public Size() {
   }

   public Size(String sizeName, int stockQuantity) {
      this.sizeName = sizeName;
      this.stockQuantity = stockQuantity;
   }

   public String getSizeName() {
      return this.sizeName;
   }

   public void setSizeName(String sizeName) {
      this.sizeName = sizeName;
   }

   public int getStockQuantity() {
      return this.stockQuantity;
   }

   public void setStockQuantity(int stockQuantity) {
      this.stockQuantity = stockQuantity;
   }

   public String toString() {
      return "Size{sizeName='" + this.sizeName + "', stockQuantity=" + this.stockQuantity + "}";
   }
}
