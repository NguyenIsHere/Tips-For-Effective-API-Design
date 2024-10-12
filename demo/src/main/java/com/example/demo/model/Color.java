// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.model;

import java.util.List;

public class Color {
   private String colorName;
   private String colorCode;
   private String imageUrl;
   private List<Size> sizes;

   public Color() {
   }

   public Color(String colorName, String colorCode, String imageUrl, List<Size> sizes) {
      this.colorName = colorName;
      this.colorCode = colorCode;
      this.imageUrl = imageUrl;
      this.sizes = sizes;
   }

   public String getColorName() {
      return this.colorName;
   }

   public void setColorName(String colorName) {
      this.colorName = colorName;
   }

   public String getColorCode() {
      return this.colorCode;
   }

   public void setColorCode(String colorCode) {
      this.colorCode = colorCode;
   }

   public String getImageUrl() {
      return this.imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public List<Size> getSizes() {
      return this.sizes;
   }

   public void setSizes(List<Size> sizes) {
      this.sizes = sizes;
   }
}
