package com.ravi.budgetPlanner.model.ENUMs;

import com.ravi.budgetPlanner.model.BudgetCategory;
import lombok.Getter;

import java.util.List;

@Getter
public enum SavingCategory implements BudgetCategory {
    HOUSE("House",List.of("HOUSE")),
    TRAVEL("Travel",List.of("TRAVEL")),
    FAMILY("Family",List.of("FAMILY"));

    private final String code;
    private final List<String> subCategory;
    SavingCategory(String code, List<String> subCategory) {
        this.code = code;
        this.subCategory = subCategory;
    }
}
