package com.ravi.budgetPlanner.model;

import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
public class LedgerDTO {
    private int id;
    private String month;
    private int year;
    private LocalDate date;
    private String category;
    private BudgetTypes type;
    private String subCategory;
    @Min(value = 1, message = "Invalid Amount")
    private int amount;
}
