package com.example.demo.Service;

import com.example.demo.Model.Transaction;
import com.example.demo.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Get all transactions
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }

    // Get a single transaction by ID
    public Optional<Transaction> singleTransaction(String id) {
        return transactionRepository.findById(id);
    }

    // Create a new transaction
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // Delete a transaction by ID
    public boolean deleteTransaction(String id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get transactions by userId
    public List<Transaction> getTransactionsByUserId(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    // Get transactions by userId and recurrencePattern (daily, weekly, monthly)
    public List<Transaction> getTransactionsByUserIdAndRecurrencePattern(String userId, String recurrencePattern) {
        return transactionRepository.findByUserIdAndRecurrencePattern(userId, recurrencePattern);
    }
}
