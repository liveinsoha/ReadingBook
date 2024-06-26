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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class TestInitClass {

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
    BookContentService bookContentService;

    @Autowired
    EntityManager entityManager;

    Random random = new Random();

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public Book getBook(Long bookId) {
        return bookRepository.findById(bookId).get();
    }

    public void initOrderData() {
        Member member1 = getMember(1L);
        Member member2 = getMember(2L);
        List<Book> bookList = bookService.findAllById(Arrays.asList(1L, 2L, 3L, 4L, 5L));

        ordersService.order(member1, bookList, createOrderRequest());
        ordersService.order(member2, bookList, createOrderRequest());
    }

    public void initOrderAndReviewBigData() {
        initMemberData(); // 회원 110명 등록
        initCategoryData(); // 카테고리 등록

        for (int i = 1; i <= 10; i++) {
            Long savedBookId = initBookData(i); // 책 10권 등록
            initAuthorData(i, savedBookId); //각각 책에 작가 3명씩 등록
        }


        for (long j = 1; j <= 100; j++) { //1~100 번 회원이 각각 랜덤으로 책을 구매한다.
            Member member = getMember(j);
            List<Long> randomBookIds = getRandomBookIds();
            List<Book> randomBooks = bookService.findAllById(randomBookIds);
            ordersService.order(member, randomBooks, createOrderRequest((int) j, randomBookIds)); // 책들 구매

            for (int i = 0; i < randomBookIds.size(); i++) { //구매 책 Ids
                Book book = randomBooks.get(i); // 구매한 책
                if (random.nextInt(2) == 0) { //리뷰를 적을 수도 있고 안 적을 수도 있음
                    reviewService.review(member, book, "reviewTest" + member.getId(), random.nextInt(5) + 1); //별점 랜덤
                }
            }
        }
    }


    private List<Long> getRandomBookIds() {
        int randomCount = 1 + random.nextInt(10); // 1 ~ 10 난수 구매 권 수

        List<Long> bookIds = new ArrayList<>();
        for (int i = 0; i < randomCount; i++) {
            long bookId = 1 + random.nextLong(10); // 구매 책 Id
            bookIds.add(bookId);
        }

        List<Long> distictBookIds = bookIds.stream().distinct().collect(Collectors.toList()); // 중복 번호는 필터링
        return distictBookIds;
    }

    public void initAuthorData(int index, Long bookId) {
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

    public Long initBookData(int index) {
        MemberRegisterRequest memberRegisterRequest1 = createMemberRegisterRequest("test@example.com", "test1234", "test1234", "test1", "1999", Gender.SECRET, "01012341234");
        Long memberId1 = memberService.register(memberRegisterRequest1).getMemberId();

        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );
        // 시리즈 번호를 제목에 추가
        String title = "해리포터와 마법사의 돌 " + index;
        // 책 등록 request
        BookRegisterRequest request = createRegisterRequest(title, "123123", "포터모어",
                "2023.01.01", 0, random.nextInt(10000), 5, 1L, 0L, "21세기 최고의 책");
        return bookManageService.registerBook(request, file, memberId1);
    }


    public void initMemberData() {
        for (int i = 1; i <= 110; i++) {
            MemberRegisterRequest memberRegisterRequest1 = createMemberRegisterRequest("test" + i + "@example.com", "test1234", "test1234", "test1", "1999", Gender.SECRET, "01012341234");
            Long memberId1 = memberService.register(memberRegisterRequest1).getMemberId();
        }
    }

    public void initMemberDataSmall() {
        for (int i = 1; i <= 5; i++) {
            MemberRegisterRequest memberRegisterRequest1 = createMemberRegisterRequest("test" + i + "@example.com", "test1234", "test1234", "test1", "1999", Gender.SECRET, "01012341234");
            Long memberId1 = memberService.register(memberRegisterRequest1).getMemberId();
        }
    }

    public void initCategoryData() {
        CategoryGroupRegisterRequest categoryGroupRequest = new CategoryGroupRegisterRequest("에세이");
        Long categoryGroupId = categoryGroupService.register(categoryGroupRequest); // 카테고리 그룹 등록

        CategoryRegisterRequest categoryRequest = new CategoryRegisterRequest("판타지", categoryGroupId);
        Long categoryId = categoryService.register(categoryRequest); // 카테고리 등록
    }

    public void initHarryPotterSeriesBookAndAuthorData(int seriesNumber) {

        MemberRegisterRequest memberRegisterRequest1 = createMemberRegisterRequest("test@example.com", "test1234", "test1234", "test1", "1999", Gender.SECRET, "01012341234");
        Long memberId1 = memberService.register(memberRegisterRequest1).getMemberId();


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

        Long bookId = bookManageService.registerBook(request, file, memberId1); // 책 등록

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


    }


    public void initBookAndAuthorData() {

        MemberRegisterRequest memberRegisterRequest1 = createMemberRegisterRequest("test@example.com", "test1234", "test1234", "test1", "1999", Gender.SECRET, "01012341234");
        Long memberId1 = memberService.register(memberRegisterRequest1).getMemberId();

        MockMultipartFile file = new MockMultipartFile(
                "해리포터와 마법사의 돌",
                "해리포터와 마법사의 돌.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test".getBytes()
        );

        //첫번쨰 책 등록
        initCategoryData();

        //책 등록 request
        BookRegisterRequest request = createRegisterRequest("해리포터와 마법사의 돌", "123123", "포터모어",
                "2023.01.01", 0, 9900, 5, 1L, 0L, "21세기 최고의 책");

        Long bookId = bookManageService.registerBook(request, file, memberId1); // 책 등록

        //BookContent 등록
        BookContentRegisterRequest bookContentRegisterRequest = new BookContentRegisterRequest(bookId, "testContent1");
        bookContentService.register(bookContentRegisterRequest);

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
                "2023.01.01", 0, 9900, 5, 1L, 0L, "21세기 최고의 책");

        Long secondBookId = bookManageService.registerBook(secondBookRequest, file, memberId1);

        //두 번째 BookContent 등록
        BookContentRegisterRequest bookContentRegisterRequest2 = new BookContentRegisterRequest(secondBookId, "testContent2");
        bookContentService.register(bookContentRegisterRequest2);

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

    private OrderRequest createOrderRequest(int index, List<Long> bookIds) {
        return new OrderRequest(
                "test 외 9권", "2023020211215" + index, "test", "card",
                "test" + index + "@test", 10000, 5000, 5000, bookIds
        );
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
            passwordConfirm, String name, String birthYear, Gender gender, String phoneNo) {
        return new MemberRegisterRequest(email, password, passwordConfirm, name, birthYear, gender, phoneNo);
    }


}

