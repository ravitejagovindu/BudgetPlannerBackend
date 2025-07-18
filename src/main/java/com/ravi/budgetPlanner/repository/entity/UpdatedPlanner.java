package com.ravi.budgetPlanner.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "updated_planner")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedPlanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int year;
    private String month;
    @ManyToOne
    @JoinColumn(name = "base_planner_id")
    private BasePlanner basePlanner;
    private int updatedProjection;

}
