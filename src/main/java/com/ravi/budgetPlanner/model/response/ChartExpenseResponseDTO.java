package com.ravi.budgetPlanner.model.response;

import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChartExpenseResponseDTO {
    private String month;
    private int year;
    private String category;
    private String subCategory;
    private BudgetTypes type;
    private int amount;
}
