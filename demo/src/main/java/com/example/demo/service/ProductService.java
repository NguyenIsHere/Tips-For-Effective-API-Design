// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.Color;
import com.example.demo.model.Size;
import com.example.demo.repository.ProductRepository;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

@Service
public class ProductService {
   @Autowired
   private ProductRepository productRepository;
   // private final String BUCKET_NAME = "tips-for-effective-api-design.appspot.com";
   private final Storage storage = (Storage)StorageOptions.getDefaultInstance().getService();

   public ProductService() {
   }

   public Product updateProductColor(String productId, String colorCode, String imagePath, List<Size> sizes) {
      Product product = (Product)this.productRepository.findById(productId).orElseThrow(() -> {
         return new RuntimeException("Product not found");
      });
      product.getColors().stream().filter((color) -> {
         return color.getColorCode().equals(colorCode);
      }).findFirst().ifPresent((color) -> {
         String imageUrl = this.uploadImageToFirebase(imagePath);
         color.setImageUrl(imageUrl);
         color.setSizes(sizes);
      });
      return (Product)this.productRepository.save(product);
   }

   public Product addProduct(Product product) {
      return (Product)this.productRepository.save(product);
   }

   private String uploadImageToFirebase(String imagePath) {
      try {
         Path path = Paths.get(imagePath);
         byte[] bytes = Files.readAllBytes(path);
         String fileName = path.getFileName().toString();
         BlobId blobId = BlobId.of("tips-for-effective-api-design.appspot.com", fileName);
         BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
         this.storage.create(blobInfo, bytes, new Storage.BlobTargetOption[0]);
         return String.format("https://storage.googleapis.com/%s/%s", "tips-for-effective-api-design.appspot.com", fileName);
      } catch (Exception var7) {
         throw new RuntimeException("Failed to upload image to Firebase", var7);
      }
   }

   public List<Product> getAllProducts() {
      return this.productRepository.findAll();
   }

   public Product updateProduct(String productId, Product product) {
      Product existingProduct = this.productRepository.findById(productId).orElse(null);
      if (existingProduct == null) {
         return null;
      } else {
         product.setProductId(productId);
         return this.productRepository.save(product);
      }
   }
   
   public void deleteProduct(String productId) {
      this.productRepository.deleteById(productId);
   }

   public Product getProductById(String productId) {
      return this.productRepository.findById(productId).orElse(null);
   }

   public List<Product> searchProductsByName(String productName) {
         Product product = new Product();
         product.setName(productName);
         ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
         Example<Product> example = Example.of(product, matcher);
         return this.productRepository.findAll(example);
   }

   public List<Product> filterProductsByPrice(double minPrice, double maxPrice) {
      List<Product> allProducts = this.productRepository.findAll();
      List<Product> filteredProducts = new ArrayList<>();
      for (Product product : allProducts) {
          for (Color color : product.getColors()) {
              for (Size size : color.getSizes()) {
                  if (size.getPrice() >= minPrice && size.getPrice() <= maxPrice) {
                      filteredProducts.add(product);
                      break;
                  }
              }
          }
      }
      return filteredProducts;
  }
}
