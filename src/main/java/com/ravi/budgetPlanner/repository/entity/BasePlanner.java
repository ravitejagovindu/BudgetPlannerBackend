package com.ravi.budgetPlanner.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "base_planner")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasePlanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int year;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private BudgetType type;
    @OneToOne
    @JoinColumn(name = "category_id")
    private BudgetCategory category;
    private int projectedAmount;

}
