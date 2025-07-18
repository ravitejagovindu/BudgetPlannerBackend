package com.ravi.budgetPlanner.model.ENUMs;

import com.ravi.budgetPlanner.model.BudgetCategory;
import lombok.Getter;

import java.util.List;

@Getter
public enum IncomeCategory implements BudgetCategory {
    RAVI("Salary",List.of("Ravi's Salary")),
    SHRI("Salary",List.of("Shri's Salary")),
    SODEXO("Sodexo",List.of("Sodexo"));

    private final String code;
    private final List<String> subCategory;

    IncomeCategory(String code, List<String> subCategory) {
        this.code = code;
        this.subCategory = subCategory;
    }
}
