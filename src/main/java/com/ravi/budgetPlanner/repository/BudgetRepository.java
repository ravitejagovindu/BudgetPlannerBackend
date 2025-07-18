package com.ravi.budgetPlanner.repository;

import com.ravi.budgetPlanner.model.BudgetTypesDTO;
import com.ravi.budgetPlanner.repository.entity.BudgetSubCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetSubCategories, Integer> {

    @Query(value = "SELECT \n" +
            "    type.code as type,\n" +
            "    category.code as category,\n" +
            "    sub.code as subCategory\n" +
            "FROM\n" +
            "    budget_type type\n" +
            "        JOIN\n" +
            "    budget_category category ON category.type_id = type.id\n" +
            "        JOIN\n" +
            "    budget_sub_categories sub ON sub.category_id = category.id", nativeQuery = true)
    List<BudgetTypesDTO> getAllTypesOfBudget();

}
