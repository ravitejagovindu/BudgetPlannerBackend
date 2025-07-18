package com.ravi.budgetPlanner.model;

import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlannerDTO {
    private int baseId;
    private int updatedId;
    private int year;
    private String month;
    private String category;
    private BudgetTypes type;
    private int projected;
}
