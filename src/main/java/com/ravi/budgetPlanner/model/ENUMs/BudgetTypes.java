package com.ravi.budgetPlanner.model.ENUMs;

import com.ravi.budgetPlanner.exception.BadRequestException;
import lombok.Getter;

@Getter
public enum BudgetTypes {
    INCOME("Income"),
    SAVING("Savings"),
    INVESTMENT("Investment"),
    EXPENSE("Expense");

    private String code;

    BudgetTypes(String code) {
        this.code = code;
    }

    public static BudgetTypes fromString(String text) {
        for (BudgetTypes budgetType : BudgetTypes.values()) {
            if (budgetType.code.equalsIgnoreCase(text)) {
                return budgetType;
            }
        }
        throw new BadRequestException(ErrorCodes.BAD_DATA);
    }

}
