package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private String id;

    private Double amount;
    private String incomeExpense;  // Follow Java naming conventions (camelCase)
    private String type;
    private List<String> tags;
    private String isRecurring;
    private String recurrencePattern;
    private List<String> category;  // Reference to Category objects

    private LocalDate transactionDate;
    private String userId;
}
