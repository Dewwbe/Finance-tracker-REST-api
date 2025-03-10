package com.example.demo.Service;

import com.example.demo.Model.Category;
import com.example.demo.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Create a new category
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get category by ID
    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    // Update category
    public Category updateCategory(String id, Category categoryDetails) {
        return categoryRepository.findById(id).map(category -> {
            category.setCategoryName(categoryDetails.getCategoryName());
            category.setCategoryDescription(categoryDetails.getCategoryDescription());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // Delete category
    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
}
