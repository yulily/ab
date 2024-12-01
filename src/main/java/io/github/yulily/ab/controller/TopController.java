package io.github.yulily.ab.controller;

import io.github.yulily.ab.model.*;
import io.github.yulily.ab.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.time.YearMonth;

@Controller
public class TopController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping(path="/")
    public @ResponseBody HashMap<String, Object> getMemberWithExpenses(
            @RequestParam(value = "currentDate", required = false) String currentDateParam
    ) {
        // TODO: バリデーション
        LocalDate currentDate = StringUtils.hasText(currentDateParam) ? LocalDate.parse(currentDateParam) : LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(currentDate);

        LocalDateTime startOfMonth = currentYearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentYearMonth.atEndOfMonth().atTime(23, 59, 59);

        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        YearMonth nextYearMonth = currentYearMonth.plusMonths(1);

        User user = userRepository.findByLoginId("123");
        // TODO: user null なら login ページへ
        if (user == null) {
            throw new RuntimeException("User not found. Please log in.");
        }

        List<Long> memberIds = user.getMembers().stream()
                .map(Member::getId)
                .toList();

        Integer totalExpenses = expenseRepository.sumExpensesByUserId(user.getId(), startOfMonth, endOfMonth);
        List<Object[]> groupByMember = expenseRepository.sumExpensesByMemberIds(memberIds, startOfMonth, endOfMonth);
        List<Object[]> groupByCategory = expenseRepository.sumExpensesByUserIdGroupByCategory(user.getId(), startOfMonth, endOfMonth);

        HashMap<String, Object> response = new HashMap<>();

        response.put("previousYearMonth", !previousYearMonth.isBefore(YearMonth.from(user.getRegisteredAt())) ? previousYearMonth.toString() : null);
        response.put("currentYearMonth", currentYearMonth.toString());
        response.put("nextYearMonth", !nextYearMonth.isAfter(currentYearMonth) ? nextYearMonth.toString() : null);

        response.put("totalExpenses", totalExpenses);
        response.put("groupByMembers", groupByMember);
        response.put("groupByCategories", groupByCategory);
        return response;
    }
}