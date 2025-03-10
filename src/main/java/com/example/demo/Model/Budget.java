package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "budget")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    private String budgetId;

    private double income;
    private double expense;

    private String budgetType; // Monthly, Category Specific
    private String category; // (Optional) If the budget is category specific
    private double goal; // Extra goal

    private String goalDuration; // (week, month, year)
}
