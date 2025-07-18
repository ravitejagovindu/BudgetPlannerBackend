package com.ravi.budgetPlanner.exception;

import com.ravi.budgetPlanner.model.ENUMs.ErrorCodes;

public class DataNotFoundException extends BudgetException{

    public DataNotFoundException(ErrorCodes errorCode) {
        super(errorCode.getMessage(), errorCode.getCode());
    }
}
