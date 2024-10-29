package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.Size;
import com.example.demo.service.ProductService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/admin/products"})
@CrossOrigin(
   origins = {"http://localhost:3000"}
)
public class ProductAdminController {
   @Autowired
   private ProductService productService;

   public ProductAdminController() {
   }

   @GetMapping
   public ResponseEntity<List<Product>> getAllProducts(@RequestHeader("Authorization") String jwt) throws Exception {
      List<Product> products = this.productService.getAllProducts();
      return ResponseEntity.ok(products);
   }

   @GetMapping("/{productId}")
   public ResponseEntity<Product> getProductById(@PathVariable String productId, @RequestHeader("Authorization") String jwt) throws Exception {
      Product product = this.productService.getProductById(productId);
      if (product != null) {
         return ResponseEntity.ok(product);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
   }

   @PostMapping
   public ResponseEntity<Product> addProduct(@RequestBody Product product, @RequestHeader("Authorization") String jwt) throws Exception{
      Product savedProduct = this.productService.addProduct(product);
      return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(savedProduct);
   }

   @PostMapping({"/{productId}/colors"})
   public ResponseEntity<Product> updateProductColor(@PathVariable String productId,
         @RequestBody ColorUpdateRequest request, @RequestHeader("Authorization") String jwt) throws Exception {
      String imageUrl = request.getImageUrl();
      String colorCode = request.getColorCode();
      List<Size> sizes = request.getSizes();
      Product updatedProduct = this.productService.updateProductColor(productId, colorCode, imageUrl, sizes);
      return ResponseEntity.ok(updatedProduct);
   }
   
   @PutMapping("/{productId}")
   public ResponseEntity<Product> updateProduct(@PathVariable String productId, @RequestBody Product product,
                                                @RequestHeader("Authorization") String jwt) throws Exception {
      Product updatedProduct = this.productService.updateProduct(productId, product);
      if (updatedProduct != null) {
         return ResponseEntity.ok(updatedProduct);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
   }

   @DeleteMapping("/{productId}")
   public ResponseEntity<Void> deleteProduct(@PathVariable String productId, @RequestHeader("Authorization") String jwt) throws Exception {
      this.productService.deleteProduct(productId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
   }
}
