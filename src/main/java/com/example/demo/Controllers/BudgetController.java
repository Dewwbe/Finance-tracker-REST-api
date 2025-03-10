package com.example.demo.Controllers;

import com.example.demo.Model.Budget;
import com.example.demo.Service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    // Create a new budget
    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        return ResponseEntity.ok(budgetService.createBudget(budget));
    }

    // Get all budgets
    @GetMapping
    public ResponseEntity<List<Budget>> getAllBudgets() {
        return ResponseEntity.ok(budgetService.getAllBudgets());
    }

    // Get budget by ID
    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable String id) {
        Optional<Budget> budget = budgetService.getBudgetById(id);
        return budget.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update budget
    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable String id, @RequestBody Budget budgetDetails) {
        return ResponseEntity.ok(budgetService.updateBudget(id, budgetDetails));
    }

    // Delete budget
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable String id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
