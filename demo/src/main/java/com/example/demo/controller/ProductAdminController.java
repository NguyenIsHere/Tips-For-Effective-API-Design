package com.example.demo.controller;

import com.example.demo.DTO.ApiResult;
import com.example.demo.DTO.ProductDTO;
import com.example.demo.model.Size;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductAdminController {

      @Autowired
      private ProductService productService;

      // Lấy danh sách tất cả sản phẩm
      @GetMapping("/all")
      public ResponseEntity<ApiResult<List<ProductDTO>>> getAllProducts(
                  @RequestHeader("Authorization") String jwt) throws Exception {
            List<ProductDTO> products = productService.getAllProducts();
            ApiResult<List<ProductDTO>> result = new ApiResult<>(
                        true,
                        "Lấy danh sách sản phẩm thành công",
                        products);

            // Set Cache-Control headers
            return ResponseEntity.ok().body(result);
      }

      // Lấy 1 sản phẩm theo ID
      @GetMapping("/{productId}")
      public ResponseEntity<ApiResult<ProductDTO>> getProductById(
                  @PathVariable String productId,
                  @RequestHeader("Authorization") String jwt) throws Exception {
            ProductDTO product = productService.getProductById(productId);
            if (product != null) {
                  ApiResult<ProductDTO> result = new ApiResult<>(
                              true,
                              "Lấy sản phẩm thành công",
                              product);

                  // Set Cache-Control headers
                  return ResponseEntity.ok().body(result);
            } else {
                  ApiResult<ProductDTO> result = new ApiResult<>(
                              false,
                              "Không tìm thấy sản phẩm",
                              null);
                  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
      }

      // Tạo mới 1 sản phẩm
      @PostMapping
      public ResponseEntity<ApiResult<ProductDTO>> addProduct(
                  @RequestBody ProductDTO product,
                  @RequestHeader("Authorization") String jwt) throws Exception {
            ProductDTO savedProduct = productService.addProduct(product);

            // Trả về sản phẩm vừa tạo (hoặc bạn có thể trả về data=null nếu không cần)
            ApiResult<ProductDTO> result = new ApiResult<>(
                        true,
                        "Tạo sản phẩm thành công",
                        savedProduct);

            return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(result);
      }

      // Cập nhật màu sắc (color) của sản phẩm
      @PostMapping("/{productId}/colors")
      public ResponseEntity<ApiResult<ProductDTO>> updateProductColor(
                  @PathVariable String productId,
                  @RequestBody ColorUpdateRequest request,
                  @RequestHeader("Authorization") String jwt) throws Exception {
            String imageUrl = request.getImageUrl();
            String colorCode = request.getColorCode();
            List<Size> sizes = request.getSizes();

            ProductDTO updatedProduct = productService.updateProductColor(productId, colorCode, imageUrl, sizes);

            ApiResult<ProductDTO> result = new ApiResult<>(
                        true,
                        "Cập nhật màu cho sản phẩm thành công",
                        updatedProduct);

            return ResponseEntity.ok(result);
      }

      // Cập nhật 1 sản phẩm
      @PutMapping("/{productId}")
      public ResponseEntity<ApiResult<ProductDTO>> updateProduct(
                  @PathVariable String productId,
                  @RequestBody ProductDTO product,
                  @RequestHeader("Authorization") String jwt) throws Exception {
            ProductDTO updatedProduct = productService.updateProduct(productId, product);

            if (updatedProduct != null) {
                  ApiResult<ProductDTO> result = new ApiResult<>(
                              true,
                              "Cập nhật sản phẩm thành công",
                              updatedProduct);
                  return ResponseEntity.ok(result);
            } else {
                  ApiResult<ProductDTO> result = new ApiResult<>(
                              false,
                              "Không tìm thấy sản phẩm để cập nhật",
                              null);
                  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
      }

      // Xoá 1 sản phẩm
      @DeleteMapping("/{productId}")
      public ResponseEntity<ApiResult<Void>> deleteProduct(
                  @PathVariable String productId,
                  @RequestHeader("Authorization") String jwt) throws Exception {
            productService.deleteProduct(productId);

            // Chỉ cần trả về success & message, data = null
            ApiResult<Void> result = new ApiResult<>(
                        true,
                        "Xóa sản phẩm thành công",
                        null);
            return ResponseEntity.ok(result);
      }
}
