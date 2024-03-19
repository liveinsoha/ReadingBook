package com.example.demo.web.service;

import com.example.demo.utils.ImageUploadUtil;
import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.dto.request.*;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.repository.BookContentRepository;
import com.example.demo.web.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BookManageServiceTest {

    @Autowired
    private BookManageService bookManagementService;

    @Autowired
    private BookRepository bookRepository;

    private ImageUploadUtil imageUploadUtil;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryGroupService categoryGroupService;

    @Autowired
    private BookGroupManageService bookGroupManagementService;

    @Autowired
    private BookContentRepository bookContentRepository;

    @Autowired
    private BookContentService bookContentService;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void beforeEach(){
        imageUploadUtil = Mockito.mock(ImageUploadUtil.class);
        bookManagementService = new BookManageService(bookService, bookRepository, categoryService, imageUploadUtil, bookGroupManagementService, bookContentRepository);
    }

    @Test
    void whenBookRegistered_thenVerifyIsRegistered() {
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest); //카테고리 그룹 등록

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest); //카테고리 등록


        //책 등록 request
        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, 0L, "21세기 최고의 책");

        Long bookId = bookManagementService.registerBook(request, file); //등록

        Book book = bookService.findBook(bookId);
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

    @Test
    void whenBookUpdatedContent_thenVerifyFields() {
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);


        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, 0L, "21세기 최고의 책");

        Long bookId = bookManagementService.registerBook(request, file);

        BookUpdateRequest updateRequest = new BookUpdateRequest("홍길동전", "test", "test", "test", 1, 1, 1, categoryId, 0L,"21세기 최고의 책");
        bookManagementService.updateBookContent(updateRequest, bookId);

        Book book = bookService.findBook(bookId);

        assertThat(book.getTitle()).isEqualTo("홍길동전");
    }

    @Test
    void whenBookDeleted_thenVerifyIsDeleted() {
        //given
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);


        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, 0L,"21세기 최고의 책");
        Long bookId = bookManagementService.registerBook(request, file);

        //when
        boolean isDeleted = bookManagementService.deleteBook(bookId);

        //then
        assertThat(isDeleted).isTrue();
        assertThatThrownBy(() -> bookService.findBook(bookId))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("검색되는 도서가 없습니다. 도서 아이디를 다시 확인해주세요.");
    }

    @Test
    void whenDeletingBookHasBookContent_thenThrowException(){ //도서 내용 먼저 삭제 해야함.
        //given
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest);

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest);


        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, 0L,"21세기 최고의 책");
        Long bookId = bookManagementService.registerBook(request, file);

        BookContentRegisterRequest bookContentRegisterRequest = new BookContentRegisterRequest(bookId, "testContent");
        bookContentService.register(bookContentRegisterRequest);

        assertThatThrownBy(() -> bookManagementService.deleteBook(bookId))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("해당 도서에는 도서 내용이 있습니다. 도서 내용을 삭제한 다음에 도서를 삭제해주세요.");
    }

    private static BookRegisterRequest createRegisterRequest(String title, String isbn, String publisher, String publishingDate, 
                                                             int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId, String description) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId, description);
    }
}