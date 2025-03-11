package com.example.demo.Repository;

import com.example.demo.Model.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends MongoRepository<Budget, String> {
    Optional<Budget> findByUserIdAndBudgetType(String userId, String budgetType);
}
