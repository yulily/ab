package io.github.yulily.ab.controller;

import io.github.yulily.ab.model.Category;
import io.github.yulily.ab.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/category") // This means URL's start with /demo (after Application path)
public class CategoryController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private CategoryRepository CategoryRepository;

    @GetMapping(path="/show")
    public @ResponseBody Iterable<Category> get() {
        return CategoryRepository.findAll();
    }

    @GetMapping(path="/year")
    public @ResponseBody Iterable<Category> getByYear() {
        return CategoryRepository.findAll();
    }

    @PostMapping(path="/update") // Map ONLY POST Requests
    public @ResponseBody String update(
            @RequestParam String categoryName
    ) {
        Category n = new Category();
        n.setCategoryName(categoryName);
        CategoryRepository.save(n);
        return "Updated";
    }

    @PostMapping(path="/create") // Map ONLY POST Requests
    public @ResponseBody String create(
            @RequestParam String categoryName
    ) {
        Category n = new Category();
        n.setCategoryName(categoryName);
        CategoryRepository.save(n);
        return "Saved";
    }
}