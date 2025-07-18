package com.ravi.budgetPlanner.model.ENUMs;

import lombok.Getter;

@Getter
public enum ErrorCodes {

    NO_DATA("4004","Data Not Found"),
    BAD_DATA("4000","Invalid Data in Request"),
    INVALID_YEAR("4001","Only Current year or previous year data is available"),
    DUPLICATE_PLANNER("4002","Duplicate Planner provided. Planner with same combination already exists"),
    INTERNAL("5001","Please Try Again");

    private String code;
    private String message;

    ErrorCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
