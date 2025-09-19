package com.ravi.budgetPlanner.repository;

import com.ravi.budgetPlanner.repository.entity.PaymentMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentModeRepository extends JpaRepository<PaymentMode,Integer> {

    Optional<PaymentMode> findPaymentModeByCode(String type);

}
