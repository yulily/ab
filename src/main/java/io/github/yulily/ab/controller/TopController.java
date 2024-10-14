package io.github.yulily.ab.controller;

import io.github.yulily.ab.model.*;
import io.github.yulily.ab.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public @ResponseBody HashMap<String, Object> getMemberWithExpenses() {
        LocalDate currentDate = LocalDate.now();
        YearMonth yearMonth = YearMonth.from(currentDate);

        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        User user = userRepository.findByLoginId("123");

        HashMap<String, Object> response = new HashMap<>();
        if (user != null) {
            List<Long> memberIds = user.getMembers().stream()
                    .map(Member::getId)
                    .toList();

            Integer totalExpenses = expenseRepository.sumExpensesByUserId(user.getId(), startOfMonth, endOfMonth);
            List<Object[]> groupByMember = expenseRepository.sumExpensesByMemberIds(memberIds, startOfMonth, endOfMonth);
            List<Object[]> groupByCategory = expenseRepository.sumExpensesByUserIdGroupByCategory(user.getId(), startOfMonth, endOfMonth);

            response.put("totalExpenses", totalExpenses);
            response.put("groupByMembers", groupByMember);
            response.put("groupByCategories", groupByCategory);
        }
        return response;
    }
}