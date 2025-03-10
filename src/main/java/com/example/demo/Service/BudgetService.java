package com.example.demo.Service;

import com.example.demo.Model.Budget;
import com.example.demo.Repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    // Create a new budget
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    // Get all budgets
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    // Get budget by ID
    public Optional<Budget> getBudgetById(String id) {
        return budgetRepository.findById(id);
    }

    // Update budget
    public Budget updateBudget(String id, Budget budgetDetails) {
        return budgetRepository.findById(id).map(budget -> {
            budget.setIncome(budgetDetails.getIncome());
            budget.setExpense(budgetDetails.getExpense());
            budget.setBudgetType(budgetDetails.getBudgetType());
            budget.setCategory(budgetDetails.getCategory());
            budget.setGoal(budgetDetails.getGoal());
            budget.setGoalDuration(budgetDetails.getGoalDuration());
            return budgetRepository.save(budget);
        }).orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    // Delete budget
    public void deleteBudget(String id) {
        budgetRepository.deleteById(id);
    }
}
