package com.ravi.budgetPlanner.service;

import com.ravi.budgetPlanner.model.BudgetTypesDTO;
import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import com.ravi.budgetPlanner.model.response.BudgetTypesResponse;
import com.ravi.budgetPlanner.repository.BudgetRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetService {

    private final BudgetRepository budgetRepository;

    private List<BudgetTypesResponse> allBudgetTypes;
    private boolean isNewBudgetTypeAdded;

    @PostConstruct
    private void init() {
        fetchAndBuildBudgetTypes();
    }

    private void fetchAndBuildBudgetTypes() {
        allBudgetTypes=new ArrayList<>();
        List<BudgetTypesDTO> allBudgetTypesFromDb = budgetRepository.getAllTypesOfBudget();
        Map<Pair<String, String>, Map<String, List<BudgetTypesDTO>>> categoriesByType =
                allBudgetTypesFromDb.stream()
                        .collect(
                                Collectors.groupingBy(
                                        budget -> Pair.of(budget.getType(), budget.getCategory()),
                                        Collectors.groupingBy(BudgetTypesDTO::getSubCategory)));
        categoriesByType.forEach((key, value) -> {
            allBudgetTypes.add(BudgetTypesResponse.builder()
                    .type(BudgetTypes.fromString(key.getFirst()))
                    .category(key.getSecond())
                    .subCategories(new ArrayList<>(value.keySet()))
                    .build());
        });
    }

    public List<BudgetTypesResponse> getAllPlannerTypes() {
        if (isNewBudgetTypeAdded) {
            fetchAndBuildBudgetTypes();
        }
        return allBudgetTypes;
    }


}
