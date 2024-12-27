package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category){
        Category newCategory = new Category();
        newCategory.setTitle(category.getTitle());
        newCategory.setDescription(category.getDescription());
        return categoryRepository.save(newCategory);
    }

    public void deleteCategory(String categoryId){
        categoryRepository.deleteById(categoryId);
    }

    public Category getCategoryById(String categoryId){
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
}
