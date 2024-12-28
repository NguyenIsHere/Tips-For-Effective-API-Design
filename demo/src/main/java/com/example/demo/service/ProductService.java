package com.example.demo.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.model.Size;
import com.example.demo.repository.ProductRepository;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class ProductService {

   @Autowired
   private ProductRepository productRepository;

   private final Storage storage = StorageOptions.getDefaultInstance().getService();

   // ========== MAPPER ========== //
   private ProductDTO mapEntityToDTO(Product product) {
      ProductDTO dto = new ProductDTO();
      dto.setName(product.getName());
      dto.setDescription(product.getDescription());
      dto.setCategory(product.getCategory());
      dto.setColors(product.getColors());
      return dto;
   }

   private Product mapDTOToEntity(ProductDTO dto) {
      Product product = new Product();
      product.setName(dto.getName());
      product.setDescription(dto.getDescription());
      product.setCategory(dto.getCategory());
      product.setColors(dto.getColors());
      return product;
   }

   // ========== CRUD ========== //
   public ProductDTO addProduct(ProductDTO productDTO) {
      Product product = mapDTOToEntity(productDTO);
      Product saved = productRepository.save(product);
      return mapEntityToDTO(saved);
   }

   public List<ProductDTO> getAllProducts() {
      List<Product> products = productRepository.findAll();
      return products.stream().map(this::mapEntityToDTO).toList();
   }

   public ProductDTO getProductById(String productId) {
      Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
      return mapEntityToDTO(product);
   }

   public ProductDTO updateProduct(String productId, ProductDTO productDTO) {
      Product existing = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

      // Chỉ update khi giá trị != null
      if (productDTO.getName() != null) {
         existing.setName(productDTO.getName());
      }
      if (productDTO.getDescription() != null) {
         existing.setDescription(productDTO.getDescription());
      }
      if (productDTO.getCategory() != null) {
         existing.setCategory(productDTO.getCategory());
      }
      if (productDTO.getColors() != null) {
         existing.setColors(productDTO.getColors());
      }

      Product updated = productRepository.save(existing);
      return mapEntityToDTO(updated);
   }

   public void deleteProduct(String productId) {
      productRepository.deleteById(productId);
   }

   public ProductDTO updateProductColor(String productId, String colorCode, String imagePath, List<Size> sizes) {
      Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

      product.getColors().stream()
            .filter(c -> c.getColorCode().equals(colorCode))
            .findFirst()
            .ifPresent(color -> {
               String imageUrl = uploadImageToFirebase(imagePath);
               color.setImageUrl(imageUrl);
               color.setSizes(sizes);
            });

      Product saved = productRepository.save(product);
      return mapEntityToDTO(saved);
   }

   public List<ProductDTO> searchProductsByName(String productName) {
      // Tạo Product “mẫu” cho Example
      Product productExample = new Product();
      productExample.setName(productName);

      ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

      Example<Product> example = Example.of(productExample, matcher);
      List<Product> products = productRepository.findAll(example);

      return products.stream().map(this::mapEntityToDTO).toList();
   }

   public Page<ProductDTO> getProducts(Pageable pageable) {
      Page<Product> productPage = productRepository.findAll(pageable);
      return productPage.map(this::mapEntityToDTO);
   }

   // ========== UPLOAD ========== //
   private String uploadImageToFirebase(String imagePath) {
      try {
         Path path = Paths.get(imagePath);
         byte[] bytes = Files.readAllBytes(path);
         String fileName = path.getFileName().toString();

         BlobId blobId = BlobId.of("tips-for-effective-api-design.appspot.com", fileName);
         BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();

         storage.create(blobInfo, bytes);

         // Link public
         return String.format("https://storage.googleapis.com/%s/%s",
               "tips-for-effective-api-design.appspot.com",
               fileName);
      } catch (Exception e) {
         throw new RuntimeException("Failed to upload image to Firebase", e);
      }
   }
}
