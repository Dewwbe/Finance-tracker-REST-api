package com.example.demo.Service;

import com.example.demo.Model.Transaction;
import com.example.demo.Repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void createTransactionTest() {
        Transaction transaction = new Transaction(
                "T123", 100.0, "income", "salary",
                Arrays.asList("job", "monthly"), "no", null,
                Arrays.asList("C123"), LocalDate.now(), "U001"
        );

        Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);

        Assertions.assertNotNull(createdTransaction);
        Assertions.assertEquals("T123", createdTransaction.getId());
        Assertions.assertEquals(100.0, createdTransaction.getAmount());
        Assertions.assertEquals("income", createdTransaction.getIncomeExpense());
    }

    @Test
    void getAllTransactionsTest() {
        Transaction t1 = new Transaction("T123", 100.0, "income", "salary",
                Arrays.asList("job", "monthly"), "no", null,
                Arrays.asList("C123"), LocalDate.now(), "U001");

        Transaction t2 = new Transaction("T124", 50.0, "expense", "groceries",
                Arrays.asList("food", "weekly"), "no", null,
                Arrays.asList("C124"), LocalDate.now(), "U002");

        Mockito.when(transactionRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Transaction> transactions = transactionService.allTransactions();

        Assertions.assertEquals(2, transactions.size());
        Assertions.assertEquals("T123", transactions.get(0).getId());
        Assertions.assertEquals("income", transactions.get(0).getIncomeExpense());
        Assertions.assertEquals("expense", transactions.get(1).getIncomeExpense());
    }

    @Test
    void getTransactionByIdTest() {
        Transaction transaction = new Transaction("T123", 100.0, "income", "salary",
                Arrays.asList("job", "monthly"), "no", null,
                Arrays.asList("C123"), LocalDate.now(), "U001");

        Mockito.when(transactionRepository.findTransactionById("T123")).thenReturn(Optional.of(transaction));

        Optional<Transaction> foundTransaction = transactionService.singleTransaction("T123");

        Assertions.assertTrue(foundTransaction.isPresent());
        Assertions.assertEquals("T123", foundTransaction.get().getId());
        Assertions.assertEquals(100.0, foundTransaction.get().getAmount());
    }

    @Test
    void deleteTransactionTest() {
        Mockito.when(transactionRepository.existsById("T123")).thenReturn(true);
        Mockito.doNothing().when(transactionRepository).deleteById("T123");

        boolean isDeleted = transactionService.deleteTransaction("T123");

        Assertions.assertTrue(isDeleted);
        Mockito.verify(transactionRepository, Mockito.times(1)).deleteById("T123");
    }

    @Test
    void deleteTransaction_NotFoundTest() {
        Mockito.when(transactionRepository.existsById("T999")).thenReturn(false);

        boolean isDeleted = transactionService.deleteTransaction("T999");

        Assertions.assertFalse(isDeleted);
        Mockito.verify(transactionRepository, Mockito.never()).deleteById("T999");
    }
}
