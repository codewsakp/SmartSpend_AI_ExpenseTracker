
package com.sakshi.expensetracker.repository;

import com.sakshi.expensetracker.entity.Expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.expenseDate >= :startDate
        AND e.expenseDate < :endDate
        """)
    BigDecimal calculateMonthlyExpenses(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    @Query("""
    SELECT e.category, SUM(e.amount)
    FROM Expense e
    WHERE e.expenseDate >= :startDate
    AND e.expenseDate < :endDate
    GROUP BY e.category
    ORDER BY SUM(e.amount) DESC
    """)
    List<Object[]> findCategoryWiseExpenses(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    List<Expense> findAllByOrderByExpenseDateDescIdDesc(
            Pageable pageable
    );
}
