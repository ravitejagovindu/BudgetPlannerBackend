package com.ravi.budgetPlanner.model.response;

import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChartResponseDTO {
    private String month;
    private int year;
    private String category;
    private BudgetTypes type;
    private int projected;
    private int actual;
    private int difference;
}
