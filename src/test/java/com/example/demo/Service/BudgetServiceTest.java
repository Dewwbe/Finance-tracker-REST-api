package com.example.demo.Service;

import com.example.demo.Model.Budget;
import com.example.demo.Repository.BudgetRepository;
import com.example.demo.Service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class BudgetServiceTest {

    @InjectMocks
    private BudgetService budgetService;

    @Mock
    private BudgetRepository budgetRepository;

    private Budget budget;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        budget = new Budget("B123", 5000, 2000, "Monthly", "Food", 3000, "Month");
    }

    // Test createBudget
    @Test
    void testCreateBudget() {
        Mockito.when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget createdBudget = budgetService.createBudget(budget);

        assertNotNull(createdBudget);
        assertEquals("B123", createdBudget.getBudgetId());
        assertEquals(5000, createdBudget.getIncome());
        assertEquals(2000, createdBudget.getExpense());
    }

    // Test getAllBudgets
    @Test
    void testGetAllBudgets() {
        Mockito.when(budgetRepository.findAll()).thenReturn(Arrays.asList(budget));

        var budgets = budgetService.getAllBudgets();

        assertNotNull(budgets);
        assertEquals(1, budgets.size());
        assertEquals("B123", budgets.get(0).getBudgetId());
    }

    // Test getBudgetById - Found
    @Test
    void testGetBudgetByIdFound() {
        Mockito.when(budgetRepository.findById("B123")).thenReturn(Optional.of(budget));

        Optional<Budget> foundBudget = budgetService.getBudgetById("B123");

        assertTrue(foundBudget.isPresent());
        assertEquals("B123", foundBudget.get().getBudgetId());
    }

    // Test getBudgetById - Not Found
    @Test
    void testGetBudgetByIdNotFound() {
        Mockito.when(budgetRepository.findById("B999")).thenReturn(Optional.empty());

        Optional<Budget> foundBudget = budgetService.getBudgetById("B999");

        assertFalse(foundBudget.isPresent());
    }

    // Test updateBudget
    @Test
    void testUpdateBudget() {
        Budget updatedBudget = new Budget("B123", 6000, 2500, "Category Specific", "Entertainment", 4000, "Month");

        Mockito.when(budgetRepository.findById("B123")).thenReturn(Optional.of(budget));
        Mockito.when(budgetRepository.save(any(Budget.class))).thenReturn(updatedBudget);

        Budget result = budgetService.updateBudget("B123", updatedBudget);

        assertNotNull(result);
        assertEquals("B123", result.getBudgetId());
        assertEquals(6000, result.getIncome());
        assertEquals("Entertainment", result.getCategory());
    }

    // Test deleteBudget
    @Test
    void testDeleteBudget() {
        Mockito.doNothing().when(budgetRepository).deleteById("B123");

        budgetService.deleteBudget("B123");

        Mockito.verify(budgetRepository, Mockito.times(1)).deleteById("B123");
    }
}
