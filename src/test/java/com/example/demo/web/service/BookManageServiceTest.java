package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.dto.request.BookRegisterRequest;
import com.example.demo.web.dto.request.CategoryGroupRegisterRequest;
import com.example.demo.web.dto.request.CategoryRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BookManageServiceTest {

    @Autowired
    private BookManageService bookManagementService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookGroupManageService bookGroupManagementService;

    @Test
    void whenBookRegistered_thenVerifyIsRegistered(){
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryService.registerCategoryGroup(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.registerCategory(categoryRequest);


        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, null);

        Long bookId = bookManagementService.registerBook(request, file);

        Book book = bookManagementService.findBookById(bookId);
        assertThat(book.getTitle()).isEqualTo("해리포터와 마법사의 돌");
        assertThat(book.getIsbn()).isEqualTo("123123");
        assertThat(book.getPublisher()).isEqualTo("포터모어");
        assertThat(book.getPublishingDate()).isEqualTo("2023.01.01");
        assertThat(book.getPaperPrice()).isEqualTo(0);
        assertThat(book.getEbookPrice()).isEqualTo(9900);
        assertThat(book.getDiscountRate()).isEqualTo(5);
        assertThat(book.getCategory().getId()).isEqualTo(categoryId);
        assertThat(book.getBookGroup()).isNull();

    }

    private static BookRegisterRequest createRegisterRequest(String title, String isbn, String publisher, String publishingDate, int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId);
    }
}