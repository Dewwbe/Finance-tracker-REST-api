package com.example.demo.Controllers;

import com.example.demo.Model.Transaction;
import com.example.demo.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Get all transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.allTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Get a transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        Optional<Transaction> transaction = transactionService.singleTransaction(id);
        return transaction.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    // ✅ Update a transaction
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id, @RequestBody Transaction updatedTransaction) {
        Optional<Transaction> transaction = transactionService.updateTransaction(id, updatedTransaction);
        return transaction.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete a transaction by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        boolean deleted = transactionService.deleteTransaction(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get transactions by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable String userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Get transactions by userId and recurrencePattern
    @GetMapping("/user/{userId}/recurrence/{recurrencePattern}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserIdAndRecurrencePattern(
            @PathVariable String userId, @PathVariable String recurrencePattern) {
        List<Transaction> transactions = transactionService.getTransactionsByUserIdAndRecurrencePattern(userId, recurrencePattern);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // ✅ Get transactions by userId and type (income/expense)
    @GetMapping("/user/{userId}/incomeExpense/{incomeExpense}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserIdAndType(
            @PathVariable String userId, @PathVariable String incomeExpense) {
        List<Transaction> transactions = transactionService.getTransactionsByUserIdAndType(userId, incomeExpense);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
    // ✅ Get total income and expense separately for a user
    @GetMapping("/user/{userId}/totals")
    public ResponseEntity<?> getTotalIncomeAndExpenseByUserId(@PathVariable String userId) {
        double totalIncome = transactionService.calculateTotalByUserIdAndType(userId, "income");
        double totalExpense = transactionService.calculateTotalByUserIdAndType(userId, "expense");

        return ResponseEntity.ok(Map.of(
                "totalIncome", totalIncome,
                "totalExpense", totalExpense
        ));
    }
    @GetMapping("/user/{userId}/income-expense-difference")
    public ResponseEntity<String> getIncomeExpenseDifference(@PathVariable String userId) {
        String result = transactionService.calculateIncomeExpenseDifference(userId);
        return ResponseEntity.ok(result);
    }



}
