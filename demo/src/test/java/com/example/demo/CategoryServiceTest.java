package com.example.demo;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() {
        // Arrange
        Category category = new Category();
        category.setTitle("Test Title");
        category.setDescription("Test Description");

        Category savedCategory = new Category();
        savedCategory.setId("1");
        savedCategory.setTitle("Test Title");
        savedCategory.setDescription("Test Description");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Act
        Category result = categoryService.createCategory(category);

        // Assert
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testDeleteCategory() {
        // Arrange
        String categoryId = "1";

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void testGetCategoryById() {
        // Arrange
        String categoryId = "1";
        Category category = new Category();
        category.setId(categoryId);
        category.setTitle("Test Title");
        category.setDescription("Test Description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        Category result = categoryService.getCategoryById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testGetCategoryByIdNotFound() {
        // Arrange
        String categoryId = "1";
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act
        Category result = categoryService.getCategoryById(categoryId);

        // Assert
        assertNull(result);
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testGetAllCategory() {
        // Arrange
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category();
        category1.setId("1");
        category1.setTitle("Title 1");
        category1.setDescription("Description 1");

        Category category2 = new Category();
        category2.setId("2");
        category2.setTitle("Title 2");
        category2.setDescription("Description 2");

        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<Category> result = categoryService.getAllCategory();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Title 2", result.get(1).getTitle());
        verify(categoryRepository, times(1)).findAll();
    }
}