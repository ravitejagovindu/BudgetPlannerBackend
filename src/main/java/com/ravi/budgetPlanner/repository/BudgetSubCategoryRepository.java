package com.ravi.budgetPlanner.repository;

import com.ravi.budgetPlanner.repository.entity.BudgetSubCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetSubCategoryRepository extends JpaRepository<BudgetSubCategories,Integer> {

    Optional<BudgetSubCategories> findBudgetCategoryByCode(String category);
    List<BudgetSubCategories> findBudgetSubCategoriesByCategory_CodeAndCodeIn(String category, List<String> subCategories);

}
