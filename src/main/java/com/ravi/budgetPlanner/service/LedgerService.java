package com.ravi.budgetPlanner.service;

import com.ravi.budgetPlanner.exception.BadRequestException;
import com.ravi.budgetPlanner.exception.BudgetException;
import com.ravi.budgetPlanner.exception.DataNotFoundException;
import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import com.ravi.budgetPlanner.model.ENUMs.ErrorCodes;
import com.ravi.budgetPlanner.model.ENUMs.Accounts;
import com.ravi.budgetPlanner.model.LedgerDTO;
import com.ravi.budgetPlanner.model.PlannerDTO;
import com.ravi.budgetPlanner.model.response.ChartExpenseResponseDTO;
import com.ravi.budgetPlanner.model.response.ChartResponseDTO;
import com.ravi.budgetPlanner.model.response.IndividualResponseDTO;
import com.ravi.budgetPlanner.repository.*;
import com.ravi.budgetPlanner.repository.entity.*;
import com.ravi.budgetPlanner.util.ValidatorHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@AllArgsConstructor
@Slf4j
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final BudgetTypeRepository budgetTypeRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final BudgetSubCategoryRepository budgetSubCategoryRepository;
    private final PaymentModeRepository paymentModeRepository;
    private final ValidatorHelper validator;

    public List<ChartResponseDTO> getLedgersForDashboard(int year, List<PlannerDTO> basePlanners, List<UpdatedPlanner> updatedPlanners) {
        List<ChartResponseDTO> chartResponse = new ArrayList<>();
        List<Ledger> dbLedgers = ledgerRepository.findLedgerByYear(year);
        List<LedgerDTO> transformedLedgers = transformDbToDTO(dbLedgers);
        Map<String, List<LedgerDTO>> ledgersByMonthAndCategory =
                transformedLedgers.stream()
                        .collect(
                                groupingBy(
                                        ledger ->
                                                ledger.getMonth().concat(ledger.getType().getCode()).concat(ledger.getCategory())
                                )
                        );
        ledgersByMonthAndCategory.forEach((key, ledgers) -> {
            int totalAmount = ledgers.stream().map(LedgerDTO::getAmount).reduce(0, Integer::sum);
            LedgerDTO ledger = ledgers.getFirst();
            chartResponse.add(
                    ChartResponseDTO
                            .builder()
                            .year(ledger.getYear())
                            .month(ledger.getMonth())
                            .type(ledger.getType())
                            .category(ledger.getCategory())
                            .actual(totalAmount)
                            .build());

        });

        basePlanners.forEach(planner -> {
            chartResponse.forEach(chart -> {
                if (chart.getYear() == planner.getYear()
                    && chart.getType().equals(planner.getType())
                    && chart.getCategory().equalsIgnoreCase(planner.getCategory())) {
                    chart.setProjected(planner.getProjected());
                }
            });
        });

        updatedPlanners.forEach(planner -> {
            chartResponse.forEach(chart -> {
                if (chart.getMonth().equalsIgnoreCase(planner.getMonth())
                    && chart.getYear() == planner.getYear()
                    && chart.getType().getCode().equals(planner.getBasePlanner().getType().getCode())
                    && chart.getCategory().equals(planner.getBasePlanner().getCategory().getCode())) {
                    chart.setProjected(planner.getUpdatedProjection());
                }
            });
        });

        chartResponse.forEach(chart -> {
            chart
                    .setDifference(
                            chart.getType().equals(BudgetTypes.EXPENSE)
                                    ? chart.getProjected() - chart.getActual()
                                    : chart.getActual() - chart.getProjected()
                    );
        });

        return chartResponse;
    }

    public List<LedgerDTO> getAllLedgers(int year, int month) {
        List<Ledger> ledgers = ledgerRepository.findLedgerByMonthAndYear(Month.of(month).name(), year);
        return transformDbToDTO(ledgers);
    }

    public Map<BudgetTypes, Integer> getActualsByType(int year, int month) {
        List<Ledger> allLedgers = ledgerRepository.findLedgerByMonthAndYear(Month.of(month).name(), year);
        Map<BudgetTypes, List<Ledger>> ledgersByType = allLedgers.stream().collect(Collectors.groupingBy(ledger -> BudgetTypes.fromString(ledger.getType().getCode())));
        Map<BudgetTypes, Integer> actualsByType = new HashMap<>();
        ledgersByType.forEach((type, planners) -> {
            int totalAmount = planners.stream().map(Ledger::getAmount).reduce(0, Integer::sum);
            actualsByType.put(type, totalAmount);
        });
        return actualsByType;
    }

    public List<IndividualResponseDTO> getIndividualBalances(int year, int month) {
        List<Ledger> allLedgers = ledgerRepository.findLedgerByMonthAndYear(Month.of(month).name(), year);
        Map<Accounts, List<Ledger>> nonIncomeIndividualLedgers = allLedgers.stream().filter(ledger -> !ledger.getType().getCode().equalsIgnoreCase(BudgetTypes.INCOME.getCode())).collect(Collectors.groupingBy(ledger -> Accounts.fromString(ledger.getPaidBy().getCode())));
        List<IndividualResponseDTO> individualBalances = new ArrayList<>();
        Map<Accounts, Integer> individualIncome = new HashMap<>();
        Map<String, List<Ledger>> incomeLedgers = allLedgers.stream().filter(ledger -> ledger.getType().getCode().equalsIgnoreCase(BudgetTypes.INCOME.getCode())).collect(Collectors.groupingBy(ledger -> ledger.getCategory().getCode()));
        incomeLedgers.forEach((category, ledgers) -> {
            int totalIncome = ledgers.stream().map(Ledger::getAmount).reduce(0, Integer::sum);
            Accounts account = Accounts.RAVI;
            if (category.equals("Shri's Salary")) {
                account = Accounts.SHRI;
            }
            individualIncome.put(account, totalIncome);
        });
        nonIncomeIndividualLedgers.forEach((account, ledgers) -> {
            int spent = ledgers.stream().map(Ledger::getAmount).reduce(0, Integer::sum);
            int income = individualIncome.get(account);
            int balance = income - spent;
            individualBalances.add(
                    IndividualResponseDTO
                            .builder()
                            .name(account.getCode())
                            .income(income)
                            .spent(spent)
                            .balance(balance)
                            .build()
            );
        });
        return individualBalances;
    }

    private List<LedgerDTO> transformDbToDTO(List<Ledger> ledgers) {
        return ledgers
                .stream()
                .map(
                        ledger ->
                                LedgerDTO
                                        .builder()
                                        .id(ledger.getId())
                                        .month(ledger.getMonth())
                                        .year(ledger.getYear())
                                        .date(ledger.getDate())
                                        .type(BudgetTypes.fromString(ledger.getType().getCode()))
                                        .category(ledger.getCategory().getCode())
                                        .subCategory(ledger.getSubCategory().getCode())
                                        .amount(ledger.getAmount())
                                        .paidBy(ledger.getPaidBy().getCode())
                                        .build())
                .toList();
    }

    public void createLedgerDTO(LedgerDTO ledgerDTO) {
        try {
            ValidateLedgerDTO(ledgerDTO);
            BudgetType budgetType =
                    budgetTypeRepository
                            .findBudgetTypeByCode(ledgerDTO.getType().getCode())
                            .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
            BudgetCategory category =
                    budgetCategoryRepository
                            .findBudgetCategoryByCode(ledgerDTO.getCategory())
                            .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
            BudgetSubCategories subcategory =
                    budgetSubCategoryRepository
                            .findBudgetCategoryByCode(ledgerDTO.getSubCategory())
                            .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
            PaymentMode paidBy = null;
            if(!ledgerDTO.getType().equals(BudgetTypes.INCOME)) {
                paidBy =
                        paymentModeRepository.findPaymentModeByCode(ledgerDTO.getPaidBy())
                                .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
            }
            Ledger ledger = Ledger.builder()
                    .month(ledgerDTO.getMonth())
                    .year(ledgerDTO.getYear())
                    .date(ledgerDTO.getDate())
                    .type(budgetType)
                    .category(category)
                    .subCategory(subcategory)
                    .amount(ledgerDTO.getAmount())
                    .paidBy(paidBy)
                    .build();
            ledgerRepository.save(ledger);
        } catch (NumberFormatException | DateTimeException | BadRequestException bde) {
            log.error("Validation Error in creating Planner :: {}", bde.getMessage());
            throw new BudgetException(ErrorCodes.BAD_DATA.getMessage(), ErrorCodes.BAD_DATA.getCode());
        }
    }

    private void ValidateLedgerDTO(LedgerDTO ledgerDTO) {
        String monthName = validator.validateMonth(ledgerDTO.getMonth());
        validator.validateDate(ledgerDTO.getDate(), ledgerDTO.getMonth(), ledgerDTO.getYear());
        ledgerDTO.setMonth(monthName);
        validator.validateTypeCategoryAndSubCategory(ledgerDTO.getType(), ledgerDTO.getCategory(), ledgerDTO.getSubCategory());
    }

    public void updateLedger(int id, LedgerDTO ledgerDTO) {
        Ledger dbLedger = ledgerRepository.findById(id).orElseThrow(() -> new DataNotFoundException(ErrorCodes.NO_DATA));
        BudgetType budgetType =
                budgetTypeRepository
                        .findBudgetTypeByCode(ledgerDTO.getType().getCode())
                        .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
        BudgetCategory category =
                budgetCategoryRepository
                        .findBudgetCategoryByCode(ledgerDTO.getCategory())
                        .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
        BudgetSubCategories subcategory =
                budgetSubCategoryRepository
                        .findBudgetCategoryByCode(ledgerDTO.getSubCategory())
                        .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
        PaymentMode paidBy =
                paymentModeRepository.findPaymentModeByCode(ledgerDTO.getPaidBy())
                        .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
        if (budgetType.getId() != dbLedger.getType().getId() || category.getId() != dbLedger.getCategory().getId() || subcategory.getId() != dbLedger.getSubCategory().getId()) {
            throw new BadRequestException(ErrorCodes.BAD_DATA);
        }

        dbLedger.setDate(ledgerDTO.getDate());
        dbLedger.setCategory(category);
        dbLedger.setType(budgetType);
        dbLedger.setSubCategory(subcategory);
        dbLedger.setAmount(ledgerDTO.getAmount());
        dbLedger.setPaidBy(paidBy);
        ledgerRepository.save(dbLedger);
    }

    public void deleteLedger(int id) {
        ledgerRepository.deleteById(id);
    }

    public List<ChartExpenseResponseDTO> getAllExpenses(int year) {

        List<ChartExpenseResponseDTO> response = new ArrayList<>();

        List<Ledger> ledgers = ledgerRepository.findLedgerByYearAndType_Code(year, BudgetTypes.EXPENSE.getCode());

        Map<String, List<Ledger>> ledgersBySubCategory = ledgers.stream().collect(groupingBy(ledger -> ledger.getMonth().concat(ledger.getSubCategory().getCode())));

        ledgersBySubCategory.forEach((subCategory, ledgerList) -> {
            int totalAmount = ledgerList.stream().map(Ledger::getAmount).reduce(0, Integer::sum);
            Ledger ledger = ledgerList.getFirst();
            response.add(
                    ChartExpenseResponseDTO
                            .builder()
                            .month(ledger.getMonth())
                            .year(ledger.getYear())
                            .type(BudgetTypes.fromString(ledger.getType().getCode()))
                            .category(ledger.getCategory().getCode())
                            .subCategory(ledger.getSubCategory().getCode())
                            .amount(totalAmount)
                            .build()
            );
        });

        return response;
    }
}
