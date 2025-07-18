package com.ravi.budgetPlanner.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "budget_sub_categories")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BudgetSubCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private String name;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private BudgetCategory category;

}
