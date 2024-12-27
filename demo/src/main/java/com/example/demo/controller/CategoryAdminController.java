package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;
import com.google.api.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryAdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<Category> createCategory(@RequestHeader("Authorization") String jwt, @RequestBody Category category) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@RequestHeader("Authorization") String jwt, @PathVariable String id) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(categoryService.getCategoryById(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategory(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(categoryService.getAllCategory(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@RequestHeader("Authorization") String jwt, @PathVariable String id) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
