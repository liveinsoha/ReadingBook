package com.example.demo.web.controller.api.controller;

import com.example.demo.web.dto.BaseResponse;
import com.example.demo.web.dto.request.CategoryGroupRegisterRequest;
import com.example.demo.web.dto.request.CategoryGroupUpdateRequest;
import com.example.demo.web.dto.request.CategoryRegisterRequest;
import com.example.demo.web.dto.request.CategoryUpdateRequest;
import com.example.demo.web.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/manage/category")
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<Object> registerCategory(CategoryRegisterRequest request){
        categoryService.register(request);

        BaseResponse<Boolean> response = new BaseResponse(HttpStatus.CREATED, "등록이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{categoryGroupId}")
    public ResponseEntity<Object> update(@PathVariable Long categoryId, CategoryUpdateRequest request){
        categoryService.update(request, categoryId);

        BaseResponse response = new BaseResponse(HttpStatus.OK, "수정이 완료되었습니다.", true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}


