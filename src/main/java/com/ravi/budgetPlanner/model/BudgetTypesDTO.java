package com.ravi.budgetPlanner.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BudgetTypesDTO {
    private String type;
    private String category;
    private String subCategory;
}
