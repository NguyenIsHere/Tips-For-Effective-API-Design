package com.example.demo.controller;

import com.example.demo.DTO.ApiResult;
import com.example.demo.DTO.ProductDTO;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductUserController {

   @Autowired
   private ProductService productService;

   // 1. Lấy tất cả sản phẩm
   @GetMapping("/all")
   public ResponseEntity<ApiResult<List<ProductDTO>>> getAllProducts() {
      List<ProductDTO> products = productService.getAllProducts();

      ApiResult<List<ProductDTO>> result = new ApiResult<>(
            true,
            "Lấy danh sách sản phẩm thành công",
            products);

      return ResponseEntity.ok(result);
   }

   // 2. Lấy sản phẩm theo ID
   @GetMapping("/{productId}")
   public ResponseEntity<ApiResult<ProductDTO>> getProductById(@PathVariable String productId) {
      ProductDTO product = productService.getProductById(productId);

      if (product != null) {
         ApiResult<ProductDTO> result = new ApiResult<>(
               true,
               "Lấy sản phẩm thành công",
               product);
         return ResponseEntity.ok(result);
      } else {
         ApiResult<ProductDTO> result = new ApiResult<>(
               false,
               "Không tìm thấy sản phẩm",
               null);
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
      }
   }

   // 3. Tìm kiếm sản phẩm theo tên
   @GetMapping("/search/{productName}")
   public ResponseEntity<ApiResult<List<ProductDTO>>> searchProductsByName(@PathVariable String productName) {
      List<ProductDTO> products = productService.searchProductsByName(productName);

      ApiResult<List<ProductDTO>> result = new ApiResult<>(
            true,
            "Tìm kiếm sản phẩm thành công",
            products);

      return ResponseEntity.ok(result);
   }

   // 4. Lấy danh sách sản phẩm có phân trang
   @GetMapping
   public ResponseEntity<ApiResult<Page<ProductDTO>>> getProducts(Pageable pageable) {
      Page<ProductDTO> productPage = productService.getProducts(pageable);

      ApiResult<Page<ProductDTO>> result = new ApiResult<>(
            true,
            "Lấy danh sách sản phẩm (có phân trang) thành công",
            productPage);

      return ResponseEntity.ok(result);
   }
}
