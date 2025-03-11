package com.example.demo.Controllers;

import com.example.demo.Model.Transaction;
import com.example.demo.Repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TransactionControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    @Test
    void testCreateTransaction() {
        Transaction transaction = new Transaction(null, 100.0, "Income", "Salary", List.of("job"), "No", null, List.of("Work"), LocalDate.now(), "user123");

        ResponseEntity<Transaction> response = restTemplate.postForEntity("/api/transactions", transaction, Transaction.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Transaction createdTransaction = response.getBody();
        assertThat(createdTransaction).isNotNull();
        assertThat(createdTransaction.getId()).isNotNull();
    }

    @Test
    void testGetAllTransactions() {
        Transaction transaction1 = new Transaction(null, 100.0, "Income", "Salary", List.of("job"), "No", null, List.of("Work"), LocalDate.now(), "user123");
        Transaction transaction2 = new Transaction(null, 200.0, "Expense", "Rent", List.of("home"), "No", null, List.of("Bills"), LocalDate.now(), "user123");
        transactionRepository.saveAll(List.of(transaction1, transaction2));

        ResponseEntity<Transaction[]> response = restTemplate.getForEntity("/api/transactions", Transaction[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void testGetTransactionById() {
        Transaction savedTransaction = transactionRepository.save(new Transaction(null, 150.0, "Income", "Freelance", List.of("side-job"), "No", null, List.of("Work"), LocalDate.now(), "user123"));

        ResponseEntity<Transaction> response = restTemplate.getForEntity("/api/transactions/{id}", Transaction.class, savedTransaction.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAmount()).isEqualTo(150.0);
    }

    @Test
    void testGetTransactionById_NotFound() {
        ResponseEntity<Transaction> response = restTemplate.getForEntity("/api/transactions/nonexistentid", Transaction.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteTransaction() {
        Transaction savedTransaction = transactionRepository.save(new Transaction(null, 75.0, "Expense", "Groceries", List.of("food"), "No", null, List.of("Shopping"), LocalDate.now(), "user123"));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/transactions/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedTransaction.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(transactionRepository.existsById(savedTransaction.getId())).isFalse();
    }

    @Test
    void testDeleteTransaction_NonExistent() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/transactions/nonexistentid",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
