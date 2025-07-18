package com.ravi.budgetPlanner.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "ledger")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int year;
    private String month;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private BudgetType type;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private BudgetCategory category;
    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    private BudgetSubCategories subCategory;
    private int amount;

}
