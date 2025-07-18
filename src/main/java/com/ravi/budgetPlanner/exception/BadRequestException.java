package com.ravi.budgetPlanner.exception;

import com.ravi.budgetPlanner.model.ENUMs.ErrorCodes;

public class BadRequestException extends BudgetException{

    public BadRequestException(ErrorCodes errorCode) {
        super(errorCode.getMessage(), errorCode.getCode());
    }
}
