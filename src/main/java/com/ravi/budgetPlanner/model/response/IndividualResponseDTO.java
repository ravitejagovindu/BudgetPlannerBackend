package com.ravi.budgetPlanner.model.response;

import com.ravi.budgetPlanner.model.ENUMs.Accounts;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndividualResponseDTO {
    private String name;
    private int income;
    private int spent;
    private int balance;
}
