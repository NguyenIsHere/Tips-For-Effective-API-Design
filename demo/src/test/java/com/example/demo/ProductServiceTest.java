package com.example.demo;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.model.Product;
import com.example.demo.model.Size;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductDTO result = productService.addProduct(productDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        Product product = new Product();
        product.setName("Test Product");
        List<Product> productList = List.of(product);
        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<ProductDTO> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById_ProductExists() {
        // Arrange
        String productId = "123";
        Product product = new Product();
        product.setName("Test Product");
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        ProductDTO result = productService.getProductById(productId);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testGetProductById_ProductNotFound() {
        // Arrange
        String productId = "123";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductById(productId);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testUpdateProduct() {
        // Arrange
        String productId = "123";
        Product existingProduct = new Product();
        existingProduct.setName("Old Name");
        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setName("New Name");
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Act
        ProductDTO result = productService.updateProduct(productId, updatedProductDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        String productId = "123";
        doNothing().when(productRepository).deleteById(productId);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testGetProducts_Pagination() {
        // Arrange
        Product product = new Product();
        product.setName("Test Product");
        List<Product> products = List.of(product);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // Act
        Page<ProductDTO> result = productService.getProducts(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getName());
        verify(productRepository, times(1)).findAll(pageable);
    }
}
