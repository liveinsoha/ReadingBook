package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.enums.AuthorOption;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.request.*;
import com.example.demo.web.dto.response.SignUpSuccessResponse;
import com.example.demo.web.repository.BookRepository;
import com.example.demo.web.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class InitClass {

    @Autowired
    BookManageService bookManageService;

    @Autowired
    CategoryGroupService categoryGroupService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AuthorManageService authorManageService;

    @Autowired
    BookAuthorListService bookAuthorListService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Autowired
    EntityManager entityManager;

    Random random = new Random();

    public Member getMember(Long memberId) {
        return memberRepository.getReferenceById(memberId);
    }

    public Book getBook(Long bookId) {
        return bookRepository.getReferenceById(bookId);
    }

    public void initOrderData() {
        Member member = getMember(1L);
        List<Book> bookList = bookService.findAllById(Arrays.asList(1L, 2L));

        ordersService.order(member, bookList, createOrderRequest());
    }


    public void initMemberData() {

        for (int i = 1; i <= 110; i++) {
            MemberRegisterRequest memberRegisterRequest1 = createMemberRegisterRequest("test" + i + "@example.com", "test1234", "test1234", "test1", "1999", Gender.SECRET);
            Long memberId1 = memberService.register(memberRegisterRequest1).getMemberId();
        }

    }

    public void initCategoryData() {
        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest); // 카테고리 그룹 등록

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest); // 카테고리 등록
    }

    public void initHarryPotterSeriesBookAndAuthorData(int seriesNumber) {
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        // 시리즈 번호를 제목에 추가
        String title = "해리포터와 마법사의 돌 " + seriesNumber;

        // 첫번째 책 등록

        // 책 등록 request
        BookRegisterRequest request = createRegisterRequest(title, "123123", "포터모어",
                "2023.01.01", 0, random.nextInt(10000), 5, 1L, 0L, "21세기 최고의 책");

        Long bookId = bookManageService.registerBook(request, file); // 책 등록

        AuthorRegisterRequest firstAuthorRequest = createAuthorRegisterRequest("DANNY", AuthorOption.AUTHOR, "영국", "test", "1999", Gender.MEN);
        Long firstAuthorId = authorManageService.registerAuthor(firstAuthorRequest);
        AuthorRegisterRequest secondAuthorRequest = createAuthorRegisterRequest("KIM", AuthorOption.AUTHOR, "미국", "test", "1999", Gender.MEN);
        Long secondAuthorId = authorManageService.registerAuthor(secondAuthorRequest);

        AuthorRegisterRequest secondTranslatorRequest = createAuthorRegisterRequest("JACK", AuthorOption.TRANSLATOR, "대한민국", "test", "1999", Gender.MEN);
        Long translatorId = authorManageService.registerAuthor(secondTranslatorRequest);

        BookAuthorListRegisterRequest firstAuthorBookRequest = createBookAuthorListRegisterRequest(bookId, firstAuthorId, 1);
        BookAuthorListRegisterRequest secondAuthorBookRequest = createBookAuthorListRegisterRequest(bookId, secondAuthorId, 2);
        BookAuthorListRegisterRequest translatorBookRequest = createBookAuthorListRegisterRequest(bookId, translatorId, 3);

        bookAuthorListService.register(firstAuthorBookRequest);
        bookAuthorListService.register(secondAuthorBookRequest);
        bookAuthorListService.register(translatorBookRequest);
    }

    public void initReviewData() {
        initMemberData();// 회원 데이터 저장

        initCategoryData(); //카테고리 데이터 저장

        for (int i = 1; i <= 10; i++) {
            initHarryPotterSeriesBookAndAuthorData(i); //해리포터 시리즈 저장
        }

        for (long i = 1; i <= 10; i++) {
            int rating1 = 1 + random.nextInt(5); //별점 1~5 사이만 가능
            reviewService.review(getMember(1L), getBook(i), "좋은 리뷰입니다111.", rating1);

            int rating2 = 1 + random.nextInt(5); //별점 1~5 사이만 가능
            reviewService.review(getMember(2L), getBook(i), "좋은 리뷰입니다222.", rating2);
        }

    }

    public void initBookAndAuthorData() {
        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        //첫번쨰 책 등록
        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("소설");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest); //카테고리 그룹 등록

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지 소설", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest); //카테고리 등록


        //책 등록 request
        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, 0L, "21세기 최고의 책");

        Long bookId = bookManageService.registerBook(request, file); // 책 등록

        AuthorRegisterRequest firstAuthorRequest = createAuthorRegisterRequest("DANNY", AuthorOption.AUTHOR, "영국", "test", "1999", Gender.MEN);
        Long firstAuthorId = authorManageService.registerAuthor(firstAuthorRequest);
        AuthorRegisterRequest secondAuthorRequest = createAuthorRegisterRequest("KIM", AuthorOption.AUTHOR, "미국", "test", "1999", Gender.MEN);
        Long secondAuthorId = authorManageService.registerAuthor(secondAuthorRequest);

        AuthorRegisterRequest secondTranslatorRequest = createAuthorRegisterRequest("JACK", AuthorOption.TRANSLATOR, "대한민국", "test", "1999", Gender.MEN);
        Long translatorId = authorManageService.registerAuthor(secondTranslatorRequest);

        BookAuthorListRegisterRequest firstAuthorBookRequest = createBookAuthorListRegisterRequest(bookId, firstAuthorId, 1);
        BookAuthorListRegisterRequest secondAuthorBookRequest = createBookAuthorListRegisterRequest(bookId, secondAuthorId, 2);
        BookAuthorListRegisterRequest translatorBookRequest = createBookAuthorListRegisterRequest(bookId, translatorId, 3);

        bookAuthorListService.register(firstAuthorBookRequest);
        bookAuthorListService.register(secondAuthorBookRequest);
        bookAuthorListService.register(translatorBookRequest);

        // 두 번째 책 등록
        BookRegisterRequest secondBookRequest = createRegisterRequest("톰 소여의 모험", "9788952760289", "포터모어",
                "2023.01.01", 0, 9900, 5, categoryId, 0L, "21세기 최고의 책");

        Long secondBookId = bookManageService.registerBook(secondBookRequest, file);

// 세 번째 작가 등록
        AuthorRegisterRequest thirdAuthorRequest = createAuthorRegisterRequest("LEE", AuthorOption.AUTHOR, "프랑스", "test", "2000", Gender.WOMEN);
        Long thirdAuthorId = authorManageService.registerAuthor(thirdAuthorRequest);

// 네 번째 작가 등록
        AuthorRegisterRequest fourthAuthorRequest = createAuthorRegisterRequest("KIM", AuthorOption.AUTHOR, "독일", "test", "2001", Gender.MEN);
        Long fourthAuthorId = authorManageService.registerAuthor(fourthAuthorRequest);

// 다섯 번째 작가 등록
        AuthorRegisterRequest fifthAuthorRequest = createAuthorRegisterRequest("CHOI", AuthorOption.TRANSLATOR, "스페인", "test", "2002", Gender.WOMEN);
        Long fifthAuthorId = authorManageService.registerAuthor(fifthAuthorRequest);

// 두 번째 책과 관련된 작가들 등록
        BookAuthorListRegisterRequest thirdAuthorBookRequest = createBookAuthorListRegisterRequest(secondBookId, thirdAuthorId, 1);
        BookAuthorListRegisterRequest fourthAuthorBookRequest = createBookAuthorListRegisterRequest(secondBookId, fourthAuthorId, 2);
        BookAuthorListRegisterRequest fifthAuthorBookRequest = createBookAuthorListRegisterRequest(secondBookId, fifthAuthorId, 3);

        bookAuthorListService.register(thirdAuthorBookRequest);
        bookAuthorListService.register(fourthAuthorBookRequest);
        bookAuthorListService.register(fifthAuthorBookRequest);

        entityManager.flush();
        entityManager.clear();
    }

    private OrderRequest createOrderRequest() {
        return new OrderRequest(
                "test 외 2권", "2023020211215", "test", "card",
                "test@test", 10000, 5000, 5000, Arrays.asList(1L, 2L)
        );
    }

    private static BookAuthorListRegisterRequest createBookAuthorListRegisterRequest(Long bookId, Long authorId,
                                                                                     int ordinal) {
        return new BookAuthorListRegisterRequest(bookId, authorId, ordinal);
    }

    private static BookRegisterRequest createRegisterRequest(String title, String isbn, String publisher, String
            publishingDate,
                                                             int paperPrice, int ebookPrice, int discountRate, Long categoryId, Long bookGroupId, String description) {
        return new BookRegisterRequest(title, isbn, publisher, publishingDate, paperPrice, ebookPrice, discountRate, categoryId, bookGroupId, description);
    }

    private AuthorRegisterRequest createAuthorRegisterRequest(String name, AuthorOption option, String
            nationality, String description, String birthYear, Gender gender) {
        AuthorRegisterRequest authorRegisterRequest = new AuthorRegisterRequest(name, option, nationality, description, birthYear, gender);
        return authorRegisterRequest;
    }


    private MemberRegisterRequest createMemberRegisterRequest(String email, String password, String
            passwordConfirm, String name, String birthYear, Gender gender) {
        return new MemberRegisterRequest(email, password, passwordConfirm, name, birthYear, gender);
    }

}

