package com.ravi.budgetPlanner.model.ENUMs;

import com.ravi.budgetPlanner.exception.BadRequestException;
import lombok.Getter;

@Getter
public enum Accounts {
    RAVI("Ravi"),
    SHRI("Shri"),;

    private String code;

    Accounts(String code) {
        this.code = code;
    }

    public static Accounts fromString(String text) {
        for (Accounts accounts : Accounts.values()) {
            if (accounts.code.equalsIgnoreCase(text)) {
                return accounts;
            }
        }
        throw new BadRequestException(ErrorCodes.BAD_DATA);
    }

}
