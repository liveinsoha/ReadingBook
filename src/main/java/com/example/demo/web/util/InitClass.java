package com.example.demo.web.util;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.BookGroup;
import com.example.demo.web.domain.entity.Category;
import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.*;
import com.example.demo.web.repository.BookGroupRepository;
import com.example.demo.web.repository.BookRepository;
import com.example.demo.web.repository.CategoryRepository;
import com.example.demo.web.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


import java.util.Random;

@Component
@RequiredArgsConstructor
public class InitClass {

    Random random = new Random();

    private final AuthorManageService authorManageService;

    private final BookAuthorListService bookAuthorListService;

    private final BookManageService bookManageService;

    private final CategoryGroupService categoryGroupService;

    private final CategoryService categoryService;

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    private final BookGroupRepository bookGroupRepository;


    public void initData(){
        initBookGroupData();
        initCategoryData();
        initBookData(1);
        initAuthorData(1,1L);
    }

    public void initBookGroupData(){
        BookGroupRegisterRequest request = new BookGroupRegisterRequest("BookGroupTitle");
        BookGroup bookGroup = BookGroup.createBookGroup(request, "book1.png");
         bookGroupRepository.save(bookGroup);
    }

    public void initCategoryData() {
        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest); // 카테고리 그룹 등록

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest); // 카테고리 등록
    }

    public void initBookData(int index) {

        // 시리즈 번호를 제목에 추가
        String title = "해리포터와 마법사의 돌 " + index;
        // 책 등록 request
        BookRegisterRequest request = createRegisterRequest(title, "123123", "포터모어",
                "2023.01.01", 0, random.nextInt(10000), 5, 1L, 1L, "21세기 최고의 책");

        Category category = categoryRepository.getReferenceById(request.getCategoryId());
        BookGroup bookgroup = bookGroupRepository.getReferenceById(request.getBookGroupId());
        bookRepository.save(Book.createBook(request,category,bookgroup,"book1.png"));
    }

    private static BookRegisterRequest createRegisterRequest(String title, String isbn, String publisher, String
            publishingDate,
                                                             int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId, String description) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId, description);
    }

    public void initAuthorData(int index, Long bookId) { // 작가 등록
        AuthorRegisterRequest firstAuthorRequest = createAuthorRegisterRequest("DANNY" + index, AuthorOption.AUTHOR, "영국", "test", "1999", Gender.MEN);
        Long firstAuthorId = authorManageService.registerAuthor(firstAuthorRequest);
        initBookAuthorListData(bookId, firstAuthorId, 1);

        AuthorRegisterRequest secondAuthorRequest = createAuthorRegisterRequest("KIM" + index, AuthorOption.AUTHOR, "미국", "test", "1999", Gender.MEN);
        Long secondAuthorId = authorManageService.registerAuthor(secondAuthorRequest);
        initBookAuthorListData(bookId, secondAuthorId, 2);

        AuthorRegisterRequest secondTranslatorRequest = createAuthorRegisterRequest("JACK" + index, AuthorOption.TRANSLATOR, "대한민국", "test", "1999", Gender.MEN);
        Long translatorId = authorManageService.registerAuthor(secondTranslatorRequest);
        initBookAuthorListData(bookId, translatorId, 3);
    }

    public void initBookAuthorListData(Long bookId, Long authorId, int ordinal) {
        BookAuthorListRegisterRequest AuthorBookRequest = createBookAuthorListRegisterRequest(bookId, authorId, ordinal);
        bookAuthorListService.register(AuthorBookRequest);
    }

    private static BookAuthorListRegisterRequest createBookAuthorListRegisterRequest(Long bookId, Long authorId,
                                                                                     int ordinal) {
        return new BookAuthorListRegisterRequest(bookId, authorId, ordinal);
    }

    private AuthorRegisterRequest createAuthorRegisterRequest(String name, AuthorOption option, String
            nationality, String description, String birthYear, Gender gender) {
        return new AuthorRegisterRequest(name, option, nationality, description, birthYear, gender);
    }

}
