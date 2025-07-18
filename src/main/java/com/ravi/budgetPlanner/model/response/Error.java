package com.ravi.budgetPlanner.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Error {
    private String code;
    private String message;
}
