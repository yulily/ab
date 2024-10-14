package io.github.yulily.ab.controller;

import io.github.yulily.ab.model.*;
import io.github.yulily.ab.model.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path="/member") // This means URL's start with /demo (after Application path)
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping(path="/new")
    public String newModel() {
        return "new";
    }

    @GetMapping(path="/show")
    public @ResponseBody Member show(@RequestParam Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id " + id));
    }

    @PostMapping(path="/update") // Map ONLY POST Requests
    public String update(
            @RequestParam Long id,
            @RequestParam String memberName,
            RedirectAttributes redirectAttributes
    ) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id " + id));
        member.setMemberName(memberName);
        memberRepository.save(member);

        redirectAttributes.addAttribute("id", id); // リダイレクト先にパラメータを追加
        return "redirect:/show";
    }

    @PostMapping(path="/create") // Map ONLY POST Requests
    public String create(
            @RequestParam String memberName,
            RedirectAttributes redirectAttributes
    ) {
        Member member = new Member();
        member.setMemberName(memberName);
        Member savedMember = memberRepository.save(member); // 保存後にエンティティを取得

        redirectAttributes.addAttribute("id", savedMember.getId());
        return "redirect:/show";
    }

    @GetMapping(path="/month")
    public @ResponseBody HashMap<String, Object> getByYearMonth(@RequestParam("member_id") Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("未登録のメンバーです"));

        LocalDate currentDate = LocalDate.now();
        YearMonth yearMonth = YearMonth.from(currentDate);

        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Object[]> groupByDates = expenseRepository.sumExpensesByMemberGroupByDay(memberId, startOfMonth, endOfMonth);
        List<LocalDate> dateList = startOfMonth.toLocalDate().datesUntil(endOfMonth.toLocalDate().plusDays(1)).toList();

        Map<LocalDate, Long> expenseMap = groupByDates.stream()
                .collect(Collectors.toMap(
                        row ->((java.sql.Date) row[0]).toLocalDate(),
                        row -> (Long) row[1]
                ));

        List<Object[]> amountDates = new ArrayList<>();

        for (LocalDate date : dateList) {
            Long amount = expenseMap.getOrDefault(date, 0L);
            amountDates.add(new Object[]{date.toString(), amount});
        }

        HashMap<String, Object> response = new HashMap<>();

        response.put("currentMonth", YearMonth.from(currentDate));
        response.put("member", member);
        response.put("amountDates", amountDates);

        return response;
    }

    @GetMapping(path="/year")
    public @ResponseBody Iterable<Member> getByYear(@RequestParam(required = false) Long memberId) {
        return memberRepository.findAll();
    }
}