package com.example.demo.Service;

import com.example.demo.Model.Category;
import com.example.demo.Repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategoryTest() {
        Category category = new Category("C123", "house", "Expenses for accommodation");

        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        Category addedCategory = categoryService.createCategory(category);

        Assertions.assertNotNull(addedCategory);
        Assertions.assertEquals(category.getCategoryId(), addedCategory.getCategoryId());
        Assertions.assertEquals(category.getCategoryName(), addedCategory.getCategoryName());
    }

    @Test
    void getAllCategoriesTest() {
        Category category1 = new Category("C123", "house", "Expenses for accommodation");
        Category category2 = new Category("C124", "food", "Expenses for meals");

        Mockito.when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = categoryService.getAllCategories();

        Assertions.assertEquals(2, categories.size());
        Assertions.assertEquals("house", categories.get(0).getCategoryName());
        Assertions.assertEquals("food", categories.get(1).getCategoryName());
    }

    @Test
    void getCategoryByIdTest() {
        Category category = new Category("C123", "house", "Expenses for accommodation");

        Mockito.when(categoryRepository.findById("C123")).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.getCategoryById("C123");

        Assertions.assertTrue(foundCategory.isPresent());
        Assertions.assertEquals("house", foundCategory.get().getCategoryName());
    }

    @Test
    void updateCategoryTest() {
        Category existingCategory = new Category("C123", "house", "Expenses for accommodation");
        Category updatedCategory = new Category("C123", "travel", "Expenses for travel");

        Mockito.when(categoryRepository.findById("C123")).thenReturn(Optional.of(existingCategory));
        Mockito.when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);

        Category result = categoryService.updateCategory("C123", updatedCategory);

        Assertions.assertEquals("travel", result.getCategoryName());
        Assertions.assertEquals("Expenses for travel", result.getCategoryDescription());
    }

    @Test
    void deleteCategoryTest() {
        Mockito.doNothing().when(categoryRepository).deleteById("C123");

        categoryService.deleteCategory("C123");

        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById("C123");
    }
}
