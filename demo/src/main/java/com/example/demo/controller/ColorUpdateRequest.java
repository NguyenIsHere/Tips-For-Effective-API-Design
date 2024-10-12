// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.controller;

import com.example.demo.model.Size;
import java.util.List;

public class ColorUpdateRequest {
   private String imageUrl;
   private String colorCode;
   private List<Size> sizes;

   public ColorUpdateRequest() {
   }

   public ColorUpdateRequest(String imageUrl, String colorCode, List<Size> sizes) {
      this.imageUrl = imageUrl;
      this.colorCode = colorCode;
      this.sizes = sizes;
   }

   public String getImageUrl() {
      return this.imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public String getColorCode() {
      return this.colorCode;
   }

   public void setColorCode(String colorCode) {
      this.colorCode = colorCode;
   }

   public List<Size> getSizes() {
      return this.sizes;
   }

   public void setSizes(List<Size> sizes) {
      this.sizes = sizes;
   }
}
