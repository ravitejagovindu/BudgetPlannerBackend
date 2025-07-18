package com.ravi.budgetPlanner.repository;

import com.ravi.budgetPlanner.repository.entity.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetTypeRepository extends JpaRepository<BudgetType,Integer> {

    Optional<BudgetType> findBudgetTypeByCode(String type);

}
