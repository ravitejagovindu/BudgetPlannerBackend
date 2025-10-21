package com.ravi.budgetPlanner.model.request;

import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryRequest {

    private BudgetTypes type;
    private String category;
    private List<String> subcategories;
    private int amount;

}
