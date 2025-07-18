package com.ravi.budgetPlanner.exception;

import lombok.Getter;

@Getter
public class BudgetException extends RuntimeException {

    private String message;
    private String code;

    public BudgetException(String message, String code) {
        super(message);
        this.message = message;
        this.code = code;
    }
    public BudgetException(String message) {
        super(message);
        this.message = message;
    }
}
