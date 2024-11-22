// Source code is decompiled from a .class file using FernFlower decompiler.
package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping({ "/api/v1/products" })
@CrossOrigin(origins = { "http://localhost:3000" })
public class ProductUserController {
   @Autowired
   private ProductService productService;

   public ProductUserController() {
   }

   @GetMapping("/all")
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

   @GetMapping
   public Page<Product> getProducts(Pageable pageable) {
      return productService.getProducts(pageable);
   }
}
