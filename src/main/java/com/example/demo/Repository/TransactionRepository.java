package com.example.demo.Repository;

import com.example.demo.Model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction,String> {

    Optional<Transaction> findTransactionById(String id);


}
