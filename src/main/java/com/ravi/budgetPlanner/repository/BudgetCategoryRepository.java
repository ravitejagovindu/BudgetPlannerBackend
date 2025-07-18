package com.ravi.budgetPlanner.repository;

import com.ravi.budgetPlanner.repository.entity.BudgetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory,Integer> {

    Optional<BudgetCategory> findBudgetCategoryByCode(String category);

}
