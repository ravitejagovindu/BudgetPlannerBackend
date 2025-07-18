package com.ravi.budgetPlanner.repository;

import com.ravi.budgetPlanner.repository.entity.BudgetCategory;
import com.ravi.budgetPlanner.repository.entity.BudgetSubCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetSubCategoryRepository extends JpaRepository<BudgetSubCategories,Integer> {

    Optional<BudgetSubCategories> findBudgetCategoryByCode(String category);

}
