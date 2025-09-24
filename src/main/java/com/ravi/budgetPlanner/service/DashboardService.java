package com.ravi.budgetPlanner.service;

import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import com.ravi.budgetPlanner.model.ENUMs.Accounts;
import com.ravi.budgetPlanner.model.PlannerDTO;
import com.ravi.budgetPlanner.model.response.*;
import com.ravi.budgetPlanner.repository.entity.UpdatedPlanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final LedgerService ledgerService;
    private final PlannerService plannerService;

    public List<ChartResponseDTO> getAllLedgers(int year) {
        List<PlannerDTO> basePlanners = plannerService.getBaseProjections(year);
        List<UpdatedPlanner> updatedPlanners = plannerService.getAllUpdatedPlanners(year);
        return ledgerService.getLedgersForDashboard(year, basePlanners, updatedPlanners);
    }

    public List<ChartExpenseResponseDTO> getAllExpenses(int year) {
        return ledgerService.getAllExpenses(year);
    }

    public ProjectionsResponseDTO getAllProjections(int year, int month) {
        Map<BudgetTypes, Integer> projectionsByType = plannerService.getProjectionsByType(year, month);
        Map<BudgetTypes, Integer> actualsByType = ledgerService.getActualsByType(year, month);

        List<ProjectionsByType> projections = new ArrayList<>();

        int incomeP = 0, incomeA = 0, otherP = 0, otherA = 0;

        for (BudgetTypes type : BudgetTypes.values()) {
            int projected = projectionsByType.getOrDefault(type, 0);
            int actual = actualsByType.getOrDefault(type, 0);
            if (type.equals(BudgetTypes.INCOME)) {
                incomeP = projected;
                incomeA = actual;
            } else {
                otherP += projected;
                otherA += actual;
            }
            projections.add(
                    ProjectionsByType
                            .builder()
                            .type(type)
                            .projected(projected)
                            .actual(actual)
                            .difference(projected - actual)
                            .build()
            );
        }

//        int incomeProjected = projections.stream().filter(type -> type.getType().equals(BudgetTypes.INCOME)).findFirst().orElseThrow(() -> new BudgetException(ErrorCodes.INTERNAL.getMessage(), ErrorCodes.INTERNAL.getCode()));
//        int incomeActual = projections.stream().filter(type -> type.getType().equals(BudgetTypes.INCOME)).map(ProjectionsByType::getActual).reduce(0, Integer::sum);
//        ;
//        int otherActual = projections.stream().filter(type -> !type.getType().equals(BudgetTypes.INCOME)).map(ProjectionsByType::getActual).reduce(0, Integer::sum);
//        int otherProjected = projections.stream().filter(type -> !type.getType().equals(BudgetTypes.INCOME)).map(ProjectionsByType::getProjected).reduce(0, Integer::sum);
        int totalDifference = projections.stream().map(ProjectionsByType::getDifference).reduce(0, Integer::sum);
        int balanceActual = incomeA-otherA;
        int balanceProjected = incomeP-otherP;

        return ProjectionsResponseDTO
                .builder()
                .balanceActual( balanceActual)
                .balanceProjected(balanceProjected)
                .balanceDifference(balanceProjected-balanceActual)
                .projectionsByType(projections)
                .build();
    }


    public List<IndividualResponseDTO> getIndividualBalances(int year, int month) {
        return ledgerService.getIndividualBalances(year,month);
    }
}
