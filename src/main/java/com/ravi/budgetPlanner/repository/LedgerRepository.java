package com.ravi.budgetPlanner.repository;

import com.ravi.budgetPlanner.model.ENUMs.BudgetTypes;
import com.ravi.budgetPlanner.repository.entity.BudgetType;
import com.ravi.budgetPlanner.repository.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger,Integer> {

    List<Ledger> findLedgerByMonthAndYear(String month, int year);
    List<Ledger> findLedgerByYear(int year);
    List<Ledger> findLedgerByYearAndType_Code(int year, String type);

}
