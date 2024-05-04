package com.example.demo.web.controller.view.category;

import com.example.demo.web.dto.response.CategorySearchResponse;
import com.example.demo.web.service.CategoryGroupService;
import com.example.demo.web.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping()
public class CategoryViewController {

    private final CategoryService categoryService;
    private final CategoryGroupService categoryGroupService;

    @GetMapping("/register/category")
    public String registerCategory(Model model){
        model.addAttribute("selectFlag", "registerCategory");
        return "manage/category/category-register";
    }

    @GetMapping("/update/category")
    public String updateCategory(Model model){
        model.addAttribute("selectFlag", "updateCategory");
        return "manage/category/category-update";
    }

    @GetMapping("/search/categories")
    public String resultForm(Model model){
        List<CategorySearchResponse> responses = categoryService.searchCategories();

        model.addAttribute("responses", responses);
        model.addAttribute("selectFlag", "searchCategories");
        return "manage/category/categories";
    }
}