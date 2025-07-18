package com.ravi.budgetPlanner.model.ENUMs;

import com.ravi.budgetPlanner.model.BudgetCategory;
import lombok.Getter;

import java.util.List;

@Getter
public enum ExpenseCategory implements BudgetCategory {
    RENT("Rent", List.of("Rent")),
    HOME("Home", List.of("Maid","Phone Bill","Wifi","Repair","Electricity","House Advance","House Shifting")),
    TRANSPORT("Transport", List.of("Office Commute","Fuel","Toll","Parking")),
    SUBSCRIPTIONS("Subscriptions", List.of("Subscriptions")),
    CREDIT_CARD("Credit Card", List.of("Credit Card")),
    VEHICLE("Vehicle", List.of("PUC","Vehicle Cleaning","Vehicle Servicing")),
    EATING_OUT("Eating Out", List.of("Swiggy","Office Food","Restaurant")),
    DOCTOR("Doctor", List.of("Doctor")),
    GROCERIES("Groceries", List.of("Offline Groceries","Online Groceries")),
    SHOPPING("Shopping", List.of("Offline Shopping","Online Shopping")),
    INSURANCE("Insurance", List.of("Health Insurance","Vehicle Insurance")),
    SALON("Salon", List.of("Salon")),
    MISCELLANEOUS("Miscellaneous", List.of("Miscellaneous"));

    private final String code;
    private final List<String> subCategory;

    ExpenseCategory(String code, List<String> subCategory) {
        this.code = code;
        this.subCategory = subCategory;
    }
}
