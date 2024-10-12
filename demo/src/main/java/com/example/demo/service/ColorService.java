// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.service;

import com.example.demo.model.Color;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ColorService {
   public ColorService() {
   }

   public void uploadImagesForColors(List<Color> colors, MultipartFile[] imageFiles) throws IOException {
      for(int i = 0; i < colors.size(); ++i) {
         Color color = (Color)colors.get(i);
         MultipartFile file = imageFiles[i];
         String imageUrl = this.uploadImageToFirebase(file);
         color.setImageUrl(imageUrl);
      }

   }

   private String uploadImageToFirebase(MultipartFile file) throws IOException {
      String var10000 = UUID.randomUUID().toString();
      String fileName = var10000 + "_" + file.getOriginalFilename();
      StorageClient.getInstance().bucket().create(fileName, file.getInputStream(), file.getContentType(), new Bucket.BlobWriteOption[0]);
      var10000 = StorageClient.getInstance().bucket().getName();
      return "https://storage.googleapis.com/" + var10000 + "/" + fileName;
   }
}
