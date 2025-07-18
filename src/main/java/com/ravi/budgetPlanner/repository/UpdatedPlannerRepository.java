package com.ravi.budgetPlanner.repository;

import com.ravi.budgetPlanner.repository.entity.BasePlanner;
import com.ravi.budgetPlanner.repository.entity.BudgetCategory;
import com.ravi.budgetPlanner.repository.entity.BudgetType;
import com.ravi.budgetPlanner.repository.entity.UpdatedPlanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UpdatedPlannerRepository extends JpaRepository<UpdatedPlanner,Integer> {

    List<UpdatedPlanner> findAllUpdatedPlannersByMonthAndYear(String month, int year);
    List<UpdatedPlanner> findAllUpdatedPlannersByYear(int year);

    Optional<UpdatedPlanner> findUpdatedPlannerByBasePlanner(BasePlanner basePlanner);
    Optional<UpdatedPlanner> findUpdatedPlannerByYearAndMonthAndBasePlanner(int year, String month, BasePlanner basePlanner);

}
