package com.example.demo.Repository;

import com.example.demo.Model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction,String> {

    Optional<Transaction> findTransactionById(String id);


    // Find transactions by userId
    List<Transaction> findByUserId(String userId);

    // Find transactions by userId and recurrencePattern
    List<Transaction> findByUserIdAndRecurrencePattern(String userId, String recurrencePattern);
}
