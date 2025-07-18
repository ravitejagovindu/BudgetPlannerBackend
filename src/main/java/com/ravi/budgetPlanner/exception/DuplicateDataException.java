package com.ravi.budgetPlanner.exception;

import com.ravi.budgetPlanner.model.ENUMs.ErrorCodes;

public class DuplicateDataException extends BudgetException{

    public DuplicateDataException(ErrorCodes errorCode) {
        super(errorCode.getMessage(), errorCode.getCode());
    }
}
