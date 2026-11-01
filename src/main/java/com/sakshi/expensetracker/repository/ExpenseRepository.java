
package com.sakshi.expensetracker.repository;

import com.sakshi.expensetracker.entity.Expense;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findAllByUserIdOrderByExpenseDateDescIdDesc(Long userId);

    Optional<Expense> findByIdAndUserId(Long id, Long userId);

    @Query("""
        SELECT COALESCE(SUM(e.amount), 0)
        FROM Expense e
        WHERE e.user.id = :userId
        AND e.expenseDate >= :startDate
        AND e.expenseDate < :endDate
        """)
    BigDecimal calculateMonthlyExpenses(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        SELECT e.category, SUM(e.amount)
        FROM Expense e
        WHERE e.user.id = :userId
        AND e.expenseDate >= :startDate
        AND e.expenseDate < :endDate
        GROUP BY e.category
        ORDER BY SUM(e.amount) DESC
        """)
    List<Object[]> findCategoryWiseExpenses(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<Expense> findAllByUserIdOrderByExpenseDateDescIdDesc(
            Long userId,
            Pageable pageable
    );
}
