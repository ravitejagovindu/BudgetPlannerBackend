package com.ravi.budgetPlanner.model.ENUMs;

import com.ravi.budgetPlanner.model.BudgetCategory;
import lombok.Getter;

import java.util.List;

@Getter
public enum InvestmentCategory implements BudgetCategory {
    MUTUAL_FUNDS("Mutual Funds",List.of("Mutual Funds")),
    STOCKS("Stocks",List.of("Stocks")),
    NPS("NPS",List.of("NPS"));

    private final String code;
    private final List<String> subCategory;
    InvestmentCategory(String code, List<String> subCategory) {
        this.code = code;
        this.subCategory = subCategory;
    }
}
