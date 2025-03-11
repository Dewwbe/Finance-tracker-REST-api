package com.example.demo.Controllers;

import com.example.demo.Model.Budget;
import com.example.demo.Repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BudgetControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BudgetRepository budgetRepository;

    @BeforeEach
    void setUp() {
        budgetRepository.deleteAll();
    }

    @Test
    void testCreateBudget() {
        Budget budget = new Budget(null, 5000.0, 2000.0, "Monthly", "Food", 1000.0, "month");

        ResponseEntity<Budget> response = restTemplate.postForEntity("/api/budgets", budget, Budget.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Budget createdBudget = response.getBody();
        assertThat(createdBudget).isNotNull();
        assertThat(createdBudget.getBudgetId()).isNotNull();
        assertThat(createdBudget.getIncome()).isEqualTo(5000.0);
        assertThat(createdBudget.getExpense()).isEqualTo(2000.0);

        Optional<Budget> savedBudget = budgetRepository.findById(createdBudget.getBudgetId());
        assertThat(savedBudget).isPresent();
        assertThat(savedBudget.get().getBudgetType()).isEqualTo("Monthly");
    }

    @Test
    void testGetAllBudgets() {
        Budget budget1 = new Budget(null, 5000.0, 2000.0, "Monthly", "Food", 1000.0, "month");
        Budget budget2 = new Budget(null, 7000.0, 3000.0, "Category Specific", "Travel", 1500.0, "year");
        budgetRepository.saveAll(List.of(budget1, budget2));

        ResponseEntity<Budget[]> response = restTemplate.getForEntity("/api/budgets", Budget[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void testGetBudgetById() {
        Budget savedBudget = budgetRepository.save(new Budget(null, 5000.0, 2000.0, "Monthly", "Food", 1000.0, "month"));

        ResponseEntity<Budget> response = restTemplate.getForEntity("/api/budgets/{id}", Budget.class, savedBudget.getBudgetId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getIncome()).isEqualTo(5000.0);
    }

    @Test
    void testGetBudgetById_NotFound() {
        ResponseEntity<Budget> response = restTemplate.getForEntity("/api/budgets/nonexistentid", Budget.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUpdateBudget() {
        Budget savedBudget = budgetRepository.save(new Budget(null, 5000.0, 2000.0, "Monthly", "Food", 1000.0, "month"));
        Budget updatedBudget = new Budget(savedBudget.getBudgetId(), 6000.0, 2500.0, "Category Specific", "Travel", 1500.0, "year");

        ResponseEntity<Budget> response = restTemplate.exchange(
                "/api/budgets/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updatedBudget),
                Budget.class,
                savedBudget.getBudgetId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getIncome()).isEqualTo(6000.0);
        assertThat(response.getBody().getCategory()).isEqualTo("Travel");

        Optional<Budget> dbBudget = budgetRepository.findById(savedBudget.getBudgetId());
        assertThat(dbBudget.get().getGoal()).isEqualTo(1500.0);
    }

    @Test
    void testUpdateBudget_NotFound() {
        Budget updatedBudget = new Budget("nonexistentid", 6000.0, 2500.0, "Category Specific", "Travel", 1500.0, "year");

        ResponseEntity<Budget> response = restTemplate.exchange(
                "/api/budgets/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updatedBudget),
                Budget.class,
                "nonexistentid"
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testDeleteBudget() {
        Budget savedBudget = budgetRepository.save(new Budget(null, 5000.0, 2000.0, "Monthly", "Food", 1000.0, "month"));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/budgets/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedBudget.getBudgetId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(budgetRepository.existsById(savedBudget.getBudgetId())).isFalse();
    }

    @Test
    void testDeleteBudget_NonExistent() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/budgets/nonexistentid",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}