package com.ravi.budgetPlanner.util;

import com.ravi.budgetPlanner.exception.BadRequestException;
import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import com.ravi.budgetPlanner.model.ENUMs.ErrorCodes;
import com.ravi.budgetPlanner.model.response.BudgetTypesResponse;
import com.ravi.budgetPlanner.service.BudgetService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Data
@AllArgsConstructor
@Component
public class ValidatorHelper {

    private final BudgetService budgetService;

    public String validateMonth(String month) {
        int monthId = Integer.parseInt(month.toUpperCase());
        return Month.of(monthId).name();
    }

    public void validateDate(LocalDate date, String month, int year) {
        if (date.getMonth().getValue() != Integer.parseInt(month) || date.getYear() != year) {
            throw new BadRequestException(ErrorCodes.BAD_DATA);
        }
    }

    public void validateTypeCategoryAndSubCategory(@NotNull @NotEmpty BudgetTypes type, @NotNull @NotEmpty String category, @NotNull @NotEmpty String subCategory) {
        List<BudgetTypesResponse> allBudgetTypes = budgetService.getAllPlannerTypes();
        BudgetTypesResponse budgetRecord =
                allBudgetTypes.stream()
                        .filter(budget ->
                                budget.getType().getCode().equals(type.getCode())
                                && budget.getCategory().equals(category))
                        .findFirst().orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));

        if (subCategory != null && !budgetRecord.getSubCategories().contains(subCategory)) {
            throw new BadRequestException(ErrorCodes.BAD_DATA);
        }

    }

}
