package com.example.demo.Service;

import com.example.demo.Model.Budget;
import com.example.demo.Model.Transaction;
import com.example.demo.Repository.BudgetRepository;
import com.example.demo.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;


    @Autowired
    private BudgetRepository budgetRepository;
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

    // ✅ Update transaction
    public Optional<Transaction> updateTransaction(String id, Transaction updatedTransaction) {
        return transactionRepository.findById(id).map(existingTransaction -> {
            existingTransaction.setAmount(updatedTransaction.getAmount());
            existingTransaction.setIncomeExpense(updatedTransaction.getIncomeExpense());
            existingTransaction.setType(updatedTransaction.getType());
            existingTransaction.setTags(updatedTransaction.getTags());
            existingTransaction.setIsRecurring(updatedTransaction.getIsRecurring());
            existingTransaction.setRecurrencePattern(updatedTransaction.getRecurrencePattern());
            existingTransaction.setCategory(updatedTransaction.getCategory());
            existingTransaction.setTransactionDate(updatedTransaction.getTransactionDate());
            existingTransaction.setUserId(updatedTransaction.getUserId());
            return transactionRepository.save(existingTransaction);
        });
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

    // ✅ Get transactions by userId and type (income/expense)
    public List<Transaction> getTransactionsByUserIdAndType(String userId, String incomeExpense) {
        return transactionRepository.findByUserIdAndIncomeExpenseIgnoreCase(userId, incomeExpense);
    }
    public double calculateTotalByUserIdAndType(String userId, String incomeExpense) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndIncomeExpense(userId, incomeExpense);
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }


    public String calculateIncomeExpenseDifference(String userId) {
        // Get the first and last day of the current month
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        // Fetch transactions for the user within the current month
        List<Transaction> transactions = transactionRepository.findByUserIdAndTransactionDateBetween(userId, startOfMonth, endOfMonth);

        double totalIncome = 0;
        double totalExpense = 0;

        // Calculate total income and expense
        for (Transaction transaction : transactions) {
            if ("income".equalsIgnoreCase(transaction.getIncomeExpense())) {
                totalIncome += transaction.getAmount();
            } else if ("expense".equalsIgnoreCase(transaction.getIncomeExpense())) {
                totalExpense += transaction.getAmount();
            }
        }

        // Fetch budget for the user where budgetType = "monthly"
        Optional<Budget> budgetOptional = budgetRepository.findByUserIdAndBudgetType(userId, "Monthly");

        if (budgetOptional.isPresent()) {
            Budget budget = budgetOptional.get();
            double difference = totalIncome - totalExpense;

            return String.format("User: %s | Monthly Budget: %.2f | Income: %.2f | Expense: %.2f | Difference: %.2f",
                    userId, budget.getGoal(), totalIncome, totalExpense, difference);
        } else {
            return "No monthly budget found for user: " + userId;
        }
    }

}
