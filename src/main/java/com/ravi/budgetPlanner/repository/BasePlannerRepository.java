package com.ravi.budgetPlanner.repository;

import com.ravi.budgetPlanner.repository.entity.BasePlanner;
import com.ravi.budgetPlanner.repository.entity.BudgetCategory;
import com.ravi.budgetPlanner.repository.entity.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasePlannerRepository extends JpaRepository<BasePlanner,Integer> {

    List<BasePlanner> findAllBasePlannersByYear(int year);

    Optional<BasePlanner> findBasePlannerByYearAndTypeAndCategory (int year, BudgetType type, BudgetCategory category);

}
