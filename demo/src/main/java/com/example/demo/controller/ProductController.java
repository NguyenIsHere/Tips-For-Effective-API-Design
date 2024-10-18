// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.Size;
import com.example.demo.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping({"/api/admin/products"})
@CrossOrigin(
   origins = {"http://localhost:3000"}
)
public class ProductController {
   @Autowired
   private ProductService productService;

   public ProductController() {
   }

   @GetMapping
   public ResponseEntity<List<Product>> getAllProducts() {
      List<Product> products = this.productService.getAllProducts();
      return ResponseEntity.ok(products);
   }

   @GetMapping("/{productId}")
   public ResponseEntity<Product> getProductById(@PathVariable String productId) {
      Product product = this.productService.getProductById(productId);
      if (product != null) {
         return ResponseEntity.ok(product);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
   }

   @GetMapping("/search/{productName}")
   public ResponseEntity<List<Product>> searchProductsByName(@PathVariable String productName) {
      List<Product> products = this.productService.searchProductsByName(productName);
      return ResponseEntity.ok(products);
   }

   @GetMapping("/filter")
   public ResponseEntity<List<Product>> filterProductsByPrice(@RequestParam double minPrice, @RequestParam double maxPrice) {
      List<Product> products = this.productService.filterProductsByPrice(minPrice, maxPrice);
      return ResponseEntity.ok(products);
   }

   @PostMapping
   public ResponseEntity<Product> addProduct(@RequestBody Product product) {
      Product savedProduct = this.productService.addProduct(product);
      return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(savedProduct);
   }

   @PostMapping({"/{productId}/colors"})
   public ResponseEntity<Product> updateProductColor(@PathVariable String productId,
         @RequestBody ColorUpdateRequest request) {
      String imageUrl = request.getImageUrl();
      String colorCode = request.getColorCode();
      List<Size> sizes = request.getSizes();
      Product updatedProduct = this.productService.updateProductColor(productId, colorCode, imageUrl, sizes);
      return ResponseEntity.ok(updatedProduct);
   }
   
   @PutMapping("/{productId}")
   public ResponseEntity<Product> updateProduct(@PathVariable String productId, @RequestBody Product product) {
      Product updatedProduct = this.productService.updateProduct(productId, product);
      if (updatedProduct != null) {
         return ResponseEntity.ok(updatedProduct);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
   }

   @DeleteMapping("/{productId}")
   public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
      this.productService.deleteProduct(productId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
   }
}
