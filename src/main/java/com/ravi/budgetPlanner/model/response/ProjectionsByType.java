package com.ravi.budgetPlanner.model.response;

import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectionsByType {
    private BudgetTypes type;
    private int projected;
    private int actual;
    private int difference;
}
