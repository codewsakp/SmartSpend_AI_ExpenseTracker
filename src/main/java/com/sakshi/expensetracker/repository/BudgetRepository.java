
package com.sakshi.expensetracker.repository;

import com.sakshi.expensetracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository
        extends JpaRepository<Budget, Long> {

    Optional<Budget> findByYearAndMonth(int year, int month);

}
