package io.github.yulily.ab.controller;

import io.github.yulily.ab.model.PaymentMethod;
import io.github.yulily.ab.model.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/payment-method") // This means URL's start with /demo (after Application path)
public class PaymentMethodController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private PaymentMethodRepository paymentMethodRepository;

    @GetMapping(path="/show")
    public @ResponseBody Iterable<PaymentMethod> get(@RequestParam("id") Long id) {
        return paymentMethodRepository.findAll();
    }

    @PostMapping(path="/update") // Map ONLY POST Requests
    public @ResponseBody String update(
            @RequestParam String name
    ) {
        PaymentMethod n = new PaymentMethod();
        n.setMethodName(name);
        paymentMethodRepository.save(n);
        return "Updated";
    }

    @PostMapping(path="/create") // Map ONLY POST Requests
    public @ResponseBody String create(
            @RequestParam String name
    ) {
        PaymentMethod n = new PaymentMethod();
        n.setMethodName(name);
        paymentMethodRepository.save(n);
        return "Saved";
    }
}