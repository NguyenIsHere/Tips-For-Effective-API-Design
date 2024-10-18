// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.model;

import java.util.List;
import lombok.Data;

@Data
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
}
