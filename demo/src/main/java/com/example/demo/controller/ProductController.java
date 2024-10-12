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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/products"})
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

   @PostMapping
   public ResponseEntity<Product> addProduct(@RequestBody Product product) {
      Product savedProduct = this.productService.addProduct(product);
      return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(savedProduct);
   }

   @PostMapping({"/{productId}/colors"})
   public ResponseEntity<Product> updateProductColor(@PathVariable String productId, @RequestBody ColorUpdateRequest request) {
      String imageUrl = request.getImageUrl();
      String colorCode = request.getColorCode();
      List<Size> sizes = request.getSizes();
      Product updatedProduct = this.productService.updateProductColor(productId, colorCode, imageUrl, sizes);
      return ResponseEntity.ok(updatedProduct);
   }
}
