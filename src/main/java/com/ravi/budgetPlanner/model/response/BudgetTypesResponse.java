package com.ravi.budgetPlanner.model.response;

import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class BudgetTypesResponse {
    private BudgetTypes type;
    private String category;
    private List<String> subCategories;
}
