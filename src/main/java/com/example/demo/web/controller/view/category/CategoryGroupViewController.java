package com.example.demo.web.controller.view.category;

import com.example.demo.web.dto.response.CategoryGroupSearchResponse;
import com.example.demo.web.service.CategoryGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoryGroupViewController {

    private final CategoryGroupService categoryGroupService;

    @GetMapping("/register/category-group")
    public String registerForm(Model model){
        model.addAttribute("selectFlag", "registerCategoryGroup");
        return "manage/category/categorygroup-register";
    }

    @GetMapping("/update/category-group")
    public String updateForm(Model model){
        model.addAttribute("selectFlag", "updateCategoryGroup");
        return "manage/category/categorygroup-update";
    }


    @GetMapping("/search/category-group")
    public String searchForm(Model model){
        model.addAttribute("selectFlag", "searchCategoryGroup");
        return "manage/categorygroup/categorygroup-search";
    }

    @GetMapping("/result/category-group")
    public String returnSearchResult(@RequestParam String name, Model model){
        CategoryGroupSearchResponse response = categoryGroupService.searchCategoryName(name);

        if(response.isSearched() == false){ //찾지 못한 경우
            model.addAttribute("isSearched", false);
        }

        model.addAttribute("isSearched", true); //찾은 경우
        model.addAttribute("response", response);
        model.addAttribute("selectFlag", "searchCategoryGroup");
        return "manage/categorygroup/categorygroup-result";
    }

    @GetMapping("/delete/category-group")
    public String deleteForm(Model model){
        model.addAttribute("selectFlag", "deleteCategoryGroup");
        return "manage/categorygroup/categorygroup-delete";
    }
}