package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.dto.request.MemberRegisterRequest;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.dto.response.SignUpSuccessResponse;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.repository.*;
import com.example.demo.web.service.email.MailService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewCommentRepository reviewCommentRepository;
    @Autowired
    private ReviewLikeLogRepository reviewLikeLogRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @BeforeEach
    void beforeEach(){
        MailService mailService = mock(MailService.class);
        memberService = new MemberService(memberRepository, passwordEncoder,
                mailService, ordersRepository,reviewRepository,
                reviewCommentRepository,reviewLikeLogRepository,bookRepository,libraryRepository);
    }

    @Test
    void register_fail_null() {
        MemberRegisterRequest request = createRequest(null, null, null, null, null, null,"01012341234");
        assertThatThrownBy(
                () -> memberService.register(request))
                .isInstanceOf(BaseException.class);
    }

    @Test
    void register_fail_invalid_email() {
        MemberRegisterRequest request = createRequest("tes@example.com", "test1234", "test1234", "test", "1999", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이메일을 올바르게 입력해주세요.");

    }

    @Test
    void register_fail_invalid_short_password() {
        MemberRegisterRequest request = createRequest("test@example.com", "test", "test", "test", "1999", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
    }

    @Test
    void register_fail_invalid_not_contained_password() {
        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test123", "test", "1999", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("비밀번호를 올바르게 입력해주세요. 비밀번호는 8-16자에 특수문자 '@, $, !, %, *, #, ?, &'가 포함되야 합니다.");
    }

    @Test
    void register_fail_invalid_name() {
        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test1234", null, "1999", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이름을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_invalid_birthYear() {
        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test1234", "test", "199", Gender.SECRET,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("생년을 올바르게 입력해주세요.");
    }


    @Test
    void register_fail_invalid_gender() {
        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test1234", "test", "1999", null,"01012341234");
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("성별을 올바르게 입력해주세요.");
    }

    @Test
    void register_fail_present_email() {
        //given
        MemberRegisterRequest registerMember = createRequest("test@example.com", "test1234", "test1234", "test", "1999", Gender.SECRET,"01012341234");
        memberService.register(registerMember);

        MemberRegisterRequest request = createRequest("test@example.com", "test1234", "test", "1999", "01012341234", Gender.MEN,"01012341234");

        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("이미 가입된 이메일입니다.");
    }

    @Test
    void register_success() {
        //given
        MemberRegisterRequest registerMember = createRequest("test@example.com", "test1234", "test1234", "test", "1999", Gender.SECRET,"01012341234");

        //when
        Long memberId = memberService.register(registerMember).getMemberId();

        //then
        assertThat(memberId).isEqualTo(2L);
    }

    @Test
    void when_PasswordChanged_then_verifyField() {
        MemberRegisterRequest registerMember = createRequest("test@example.com", "test1234", "test1234", "test", "1999", Gender.SECRET, "01012341234");
        Long memberId = memberService.register(registerMember).getMemberId();

        Member member = memberService.getMember(memberId);
        memberService.update("test1234", "asdf1234!", "asdf1234!", member);

        assertTrue(passwordEncoder.matches("asdf1234!", member.getPassword()));
    }

    @Test
    void whenChangedTempPassword_thenVerifyField(){
        //given
        MemberRegisterRequest request = new MemberRegisterRequest("test@gmail.com", "testtest1234", "testtest1234", "홍길동", "1999", Gender.SECRET, "01055555555");
        SignUpSuccessResponse signUpSuccessResponse = memberService.register(request);

        //when
        String email = "test@gmail.com";
        String tempPassword = memberService.changePasswordAndSendEmail(email, "01055555555");

        Member member = memberService.getMember(signUpSuccessResponse.getMemberId());


        //then
        assertThat(passwordEncoder.matches(tempPassword, member.getPassword())).isTrue();
    }

    private MemberRegisterRequest createRequest(String email, String password, String passwordConfirm, String name, String birthYear, Gender gender, String phoneNo) {
        return new MemberRegisterRequest(email, password, passwordConfirm, name, birthYear, gender, phoneNo);
    }


    @Autowired
    InitClass initClass;
    @Autowired
    EntityManager entityManager;

    @Test
    @Rollback(value = false)
    void when_MemberLeave_then_CheckQuery(){
        initClass.initMemberDataSmall();
        initClass.initCategoryData();
        for (int i = 1; i <= 5; i++) { //책 5개 등록
            initClass.initBookData(i);
        }
        initClass.initOrderData();

        Member member = initClass.getMember(1L);


        for (long i = 1; i <= 5; i++) { //책 5개에 각각 리뷰 작성
            Book book = initClass.getBook(i);
            Long savedId1 = reviewService.review(member, book, "reviewContent", 5);
        }
        entityManager.flush();
        entityManager.clear();


        memberService.leave(member, "test1234");

    }


}