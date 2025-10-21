package com.ravi.budgetPlanner.service;

import com.ravi.budgetPlanner.exception.BadRequestException;
import com.ravi.budgetPlanner.exception.BudgetException;
import com.ravi.budgetPlanner.exception.DataNotFoundException;
import com.ravi.budgetPlanner.exception.DuplicateDataException;
import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import com.ravi.budgetPlanner.model.ENUMs.ErrorCodes;
import com.ravi.budgetPlanner.model.PlannerDTO;
import com.ravi.budgetPlanner.model.request.CategoryRequest;
import com.ravi.budgetPlanner.repository.*;
import com.ravi.budgetPlanner.repository.entity.*;
import com.ravi.budgetPlanner.util.ValidatorHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlannerService {

    private final BasePlannerRepository basePlannerRepository;
    private final UpdatedPlannerRepository updatedPlannerRepository;
    private final BudgetTypeRepository budgetTypeRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final BudgetSubCategoryRepository budgetSubCategoryRepository;
    private final ValidatorHelper validator;

    public List<PlannerDTO> getBaseProjections(int year) {

        Map<Integer, PlannerDTO> projections = new HashMap<>();

        List<BasePlanner> basePlannersByYear = basePlannerRepository.findAllBasePlannersByYear(year);
        if (basePlannersByYear.isEmpty()) throw new BadRequestException(ErrorCodes.INVALID_YEAR);
        basePlannersByYear.forEach(base ->
                projections.put(
                        base.getId(),
                        PlannerDTO
                                .builder()
                                .baseId(base.getId())
                                .year(base.getYear())
                                .type(BudgetTypes.fromString(base.getType().getCode()))
                                .category(base.getCategory().getCode())
                                .projected(base.getProjectedAmount())
                                .build()
                ));

        return new ArrayList<>(projections.values());
    }

    public List<UpdatedPlanner> getAllUpdatedPlanners(int year) {
        return updatedPlannerRepository.findAllUpdatedPlannersByYear(year);
    }

    public List<PlannerDTO> getAllPlanners(int year, int month) {

        Map<Integer, PlannerDTO> planners = new HashMap<>();

        List<BasePlanner> basePlannersByYear = basePlannerRepository.findAllBasePlannersByYear(year);
        if (basePlannersByYear.isEmpty()) throw new BadRequestException(ErrorCodes.INVALID_YEAR);
        basePlannersByYear.forEach(base ->
                planners.put(
                        base.getId(),
                        PlannerDTO
                                .builder()
                                .baseId(base.getId())
                                .year(base.getYear())
                                .type(BudgetTypes.fromString(base.getType().getCode()))
                                .category(base.getCategory().getCode())
                                .projected(base.getProjectedAmount())
                                .build()
                ));

        List<UpdatedPlanner> updatedPlanners = updatedPlannerRepository.findAllUpdatedPlannersByMonthAndYear(Month.of(month).name(), year);

        updatedPlanners.forEach(update -> {
            PlannerDTO planner = planners.get(update.getBasePlanner().getId());
            if (planner == null) {
                log.error("UpdatedProjection record doesn't match with the base record");
                throw new BudgetException(ErrorCodes.INTERNAL.getMessage(), ErrorCodes.INTERNAL.getCode());
            }
            planner.setUpdatedId(update.getId());
            planner.setProjected(update.getUpdatedProjection());
        });

        return new ArrayList<>(planners.values());
    }

    public void createBasePlanner(PlannerDTO planner) {

        if (planner.getYear() > LocalDate.now().getYear() || planner.getYear() < LocalDate.now().getYear() - 1)
            throw new BadRequestException(ErrorCodes.INVALID_YEAR);

        BudgetType budgetType =
                budgetTypeRepository
                        .findBudgetTypeByCode(planner.getType().getCode())
                        .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
        BudgetCategory category =
                budgetCategoryRepository
                        .findBudgetCategoryByCode(planner.getCategory())
                        .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));

        basePlannerRepository.findBasePlannerByYearAndTypeAndCategory(
                        planner.getYear(),
                        budgetType,
                        category
                )
                .ifPresent(ex -> {
                    throw new DuplicateDataException(ErrorCodes.DUPLICATE_PLANNER);
                });

        basePlannerRepository.save(BasePlanner.builder()
                .year(planner.getYear())
                .type(budgetType)
                .category(category)
                .projectedAmount(planner.getProjected())
                .build());

    }

    public void updateProjection(int id, PlannerDTO planner) {
        validatePlanner(planner);

        BasePlanner basePlanner =
                basePlannerRepository.findById(id)
                        .orElseThrow(() -> new DataNotFoundException(ErrorCodes.NO_DATA));

        if (!(basePlanner.getType().getCode().equalsIgnoreCase(planner.getType().getCode())
              || basePlanner.getCategory().getCode().equalsIgnoreCase(planner.getCategory())))
            throw new BadRequestException(ErrorCodes.BAD_DATA);

        UpdatedPlanner overriddenPlanner =
                updatedPlannerRepository
                        .findUpdatedPlannerByYearAndMonthAndBasePlanner(planner.getYear(), planner.getMonth(), basePlanner)
                        .orElse(
                                UpdatedPlanner.builder()
                                        .month(planner.getMonth())
                                        .year(planner.getYear())
                                        .basePlanner(basePlanner)
                                        .build()
                        );

        if ((overriddenPlanner.getUpdatedProjection() == 0 && basePlanner.getProjectedAmount() == planner.getProjected())
            || (overriddenPlanner.getUpdatedProjection() == planner.getProjected())) {
            return;
        }

        overriddenPlanner.setUpdatedProjection(planner.getProjected());
        updatedPlannerRepository.save(overriddenPlanner);
    }

    private void validatePlanner(PlannerDTO planner) {
        try {
            String month = validator.validateMonth(planner.getMonth());
            planner.setMonth(month);
            validator.validateTypeCategoryAndSubCategory(planner.getType(), planner.getCategory(), null);
        } catch (NumberFormatException ex) {
            log.error("Validation Error in creating Planner :: {}", ex.getMessage());
            throw new BudgetException(ErrorCodes.BAD_DATA.getMessage(), ErrorCodes.BAD_DATA.getCode());
        }
    }

    public void deleteProjection(int id) {
        BasePlanner basePlanner =
                basePlannerRepository
                        .findById(id)
                        .orElseThrow(() -> new DataNotFoundException(ErrorCodes.NO_DATA));
        updatedPlannerRepository
                .findUpdatedPlannerByBasePlanner(basePlanner)
                .ifPresent(updatedPlannerRepository::delete);
    }

    public Map<BudgetTypes, Integer> getProjectionsByType(int year, int month) {
        List<PlannerDTO> allPlanners = getAllPlanners(year, month);
        Map<BudgetTypes, List<PlannerDTO>> plannersByType = allPlanners.stream().collect(Collectors.groupingBy(PlannerDTO::getType));
        Map<BudgetTypes, Integer> projectionsByType = new HashMap<>();
        plannersByType.forEach((type, planners) -> {
            int totalProjection = planners.stream().map(PlannerDTO::getProjected).reduce(0, Integer::sum);
            projectionsByType.put(type, totalProjection);
        });
        return projectionsByType;
    }

    public void createSubCategories(CategoryRequest category) {

        BudgetType type = budgetTypeRepository
                .findBudgetTypeByCode(category.getType().getCode())
                .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));

        BudgetCategory dbCategory = budgetCategoryRepository
                .findBudgetCategoryByCode(category.getCategory())
                .orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
        try {
            List<BudgetSubCategories> dbSubCategories = new ArrayList<>();

            category.getSubcategories().forEach(subCategory ->
                    dbSubCategories.add(
                            BudgetSubCategories
                                    .builder()
                                    .category(dbCategory)
                                    .code(subCategory)
                                    .name(subCategory)
                                    .build()
                    )
            );

            budgetSubCategoryRepository.saveAll(dbSubCategories);

        } catch (Exception e) {
            throw new BudgetException(ErrorCodes.INTERNAL.getMessage(), ErrorCodes.INTERNAL.getCode());
        }


    }

    public void createCategories(CategoryRequest newCategory) {
        String category = newCategory.getCategory();
        List<String> subcategories = newCategory.getSubcategories();

        if (category.isBlank() || subcategories.isEmpty()) {
            throw new BadRequestException(ErrorCodes.BAD_DATA);
        }

        budgetCategoryRepository.findBudgetCategoryByCode(category).ifPresent(cat -> {
            throw new BadRequestException(ErrorCodes.BAD_DATA);
        });

        BudgetType type = budgetTypeRepository.findBudgetTypeByCode(newCategory.getType().getCode()).orElseThrow(() -> new BadRequestException(ErrorCodes.BAD_DATA));
        try {
            BudgetCategory newDbCategory = budgetCategoryRepository.save(
                    BudgetCategory
                            .builder()
                            .code(category)
                            .name(category)
                            .type(type)
                            .build());
            List<BudgetSubCategories> dbSubCategories = new ArrayList<>();

            subcategories.forEach(subCategory ->
                    dbSubCategories.add(
                            BudgetSubCategories
                                    .builder()
                                    .category(newDbCategory)
                                    .code(subCategory)
                                    .name(subCategory)
                                    .build()
                    )
            );

            budgetSubCategoryRepository.saveAll(dbSubCategories);
        } catch (Exception e) {
            throw new BudgetException(ErrorCodes.INTERNAL.getMessage(), ErrorCodes.INTERNAL.getCode());
        }

    }
}
