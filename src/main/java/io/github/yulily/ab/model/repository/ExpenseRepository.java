package io.github.yulily.ab.model.repository;

import io.github.yulily.ab.model.Expense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.member.user.id = ?1 AND e.paymentAt BETWEEN ?2 AND ?3")
    Integer sumExpensesByUserId(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e.member.id, e.member.memberName, SUM(e.amount) FROM Expense e WHERE e.member.id IN ?1 AND e.paymentAt BETWEEN ?2 AND ?3 GROUP BY e.member.id, e.member.memberName")
    List<Object[]> sumExpensesByMemberIds(List<Long> memberIds, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c.id, c.categoryName, SUM(e.amount) FROM Expense e JOIN e.category c WHERE e.member.user.id = ?1 AND e.paymentAt BETWEEN ?2 AND ?3 GROUP BY e.category.id, c.categoryName")
    List<Object[]> sumExpensesByUserIdGroupByCategory(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT DATE(e.paymentAt), SUM(e.amount) FROM Expense e WHERE e.member.id = :memberId AND e.paymentAt BETWEEN :startOfMonth AND :endOfMonth GROUP BY DATE(e.paymentAt) ORDER BY DATE(e.paymentAt)")
    List<Object[]> sumExpensesByMemberGroupByDay(Long memberId, LocalDateTime startOfMonth, LocalDateTime endOfMonth);
}
