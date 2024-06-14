package com.example.demo.web.service;


import com.example.demo.web.domain.entity.*;
import com.example.demo.web.domain.enums.MemberRole;
import com.example.demo.web.dto.request.MemberRegisterRequest;
import com.example.demo.web.dto.response.MemberInformationResponse;
import com.example.demo.web.dto.response.ModifyMemberResponse;
import com.example.demo.web.dto.response.SignUpSuccessResponse;
import com.example.demo.web.domain.enums.Gender;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.*;
import com.example.demo.web.service.email.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final OrdersRepository ordersRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewLikeLogRepository reviewLikeLogRepository;
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BaseException(BaseResponseCode.MEMBER_NOT_FOUND));
    }

    public Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new BaseException(BaseResponseCode.MEMBER_NOT_FOUND));
    }

    public Member getMember(Principal principal) {
        if (principal == null) {
            throw new BaseException(BaseResponseCode.LOGIN_REQUIRED);
        }

        String email = principal.getName();
        return getMember(email);
    }

    public List<String> findEmail(String name, String phoneNo) {
        List<Member> members = memberRepository.findByNameAndPhoneNo(name, phoneNo);

        if (members.isEmpty()) {
            throw new BaseException(BaseResponseCode.MEMBER_NOT_FOUND);
        }

        return members.stream().map(m -> maskEmail(m.getEmail()))
                .collect(Collectors.toList());
    }

    public String maskEmail(String email) {
        int atIndex = email.indexOf('@');

        String prefix = email.substring(0, 3);
        String domain = email.substring(atIndex);
        String maskedPrefix = prefix + "*".repeat(atIndex - 3); // 빼기 3을 한 이유는, 앞 3글자는 마스킹 되지 않아야 함
        return maskedPrefix + domain;
    }

    @Transactional
    public SignUpSuccessResponse register(MemberRegisterRequest request) {
        validatePresentEmail(request.getEmail());
        validateForm(request);

        Member member = Member.createMember(request);

        String encodedPassword = passwordEncoder.encode(member.getPassword());
        member.encodePassword(encodedPassword);

        Member savedMember = memberRepository.save(member);
        return new SignUpSuccessResponse(member.getId());
    }


    public void validatePresentEmail(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);

        if (findMember.isPresent()) {
            throw new BaseException(BaseResponseCode.EMAIL_DUPLICATE);
        }
    }

    private static void validateForm(MemberRegisterRequest request) {
        String email = request.getEmail();
        if (email == null) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_EMAIL);
        }

        String[] splitEmail = email.split("@");
        if (splitEmail[0].length() < 4 || splitEmail[0].length() > 24) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_EMAIL);
        }

        String password = request.getPassword();
        String passwordConfirm = request.getPasswordConfirm();
        if (password == null || passwordConfirm == null) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_PASSWORD);
        }

        if ((password.length() < 8 || password.length() > 16) || (passwordConfirm.length() < 8 || passwordConfirm.length() > 16)) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_PASSWORD);
        }

        if (!password.equals(passwordConfirm)) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_PASSWORD);
        }


        String name = request.getName();
        if (name == null) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_NAME);
        }
        if (name.length() > 20 || name.length() < 2) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_NAME);
        }

        String birthYear = request.getBirthYear();
        if (birthYear.length() != 4) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_BIRTH_YEAR);

        }

        Gender gender = request.getGender();
        if (gender == null) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_GENDER);
        }
    }

    private void validatePhoneNo(String phoneNo) {
        if (phoneNo == null || phoneNo.trim().isEmpty()) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_PHONE_NO);
        }

        if (phoneNo.length() != 11) {
            throw new BaseException(BaseResponseCode.INVALID_FORM_PHONE_NO);
        }

    }


    /**
     * 비밀번호 확인 이후 DTO 반환 메소드
     *
     * @param rawPassword
     * @param member
     * @return dto
     */
    public ModifyMemberResponse matchPasswordThenReturnResponse(String rawPassword, Member member) {
        String encodedPassword = member.getPassword();

        matchPasswords(rawPassword, encodedPassword);

        return new ModifyMemberResponse(member);
    }

    @Transactional
    public void confirmSellerCode(String sellerCode, Member member) {
        if (!member.getSellerCode().equals(sellerCode)) {
            throw new BaseException(BaseResponseCode.INVALID_SELLER_CODE);
        }
        member.setRole(MemberRole.ROLE_ADMIN);
    }

    @Transactional
    public void sendSellerConfirmEmail(String rawPassword, Member member) {
        String encodedPassword = member.getPassword();
        matchPasswords(rawPassword, encodedPassword);

        String confirmSellCode = createConfirmSellCode();
        member.updateSellerCode(confirmSellCode);

        mailService.sendCode(member.getEmail(), confirmSellCode);
    }

    @Transactional
    public void sendBookRequestEmail(String bookTitle) {
        mailService.sendRequest(bookTitle);
    }

    private void matchPasswords(String rawPassword, String encodedPassword) {
        boolean result = passwordEncoder.matches(rawPassword, encodedPassword);

        if (result == false) {
            throw new BaseException(BaseResponseCode.INVALID_PASSWORD);
        }
    }

    /**
     * 비밀번호 업데이트
     *
     * @param password
     * @param newPassword
     * @param newPasswordConfirm
     * @param member
     */
    @Transactional
    public void update(String password, String newPassword, String newPasswordConfirm, Member member) {
        String encodedPassword = member.getPassword();

        validatePasswords(password, newPassword, newPasswordConfirm);

        matchPasswords(password, encodedPassword);

        String changingPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(changingPassword);
    }

    private void validatePasswords(String password, String newPassword, String newPasswordConfirm) {
        if (password == null || password.trim().equals("")) {
            throw new IllegalArgumentException("현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.");
        }

        if (newPassword == null || newPassword.trim().equals("")) {
            throw new IllegalArgumentException("현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.");
        }

        if (newPasswordConfirm == null || newPasswordConfirm.trim().equals("")) {
            throw new IllegalArgumentException("현재 비밀번호나 새 비밀번호 또는 새 비밀번호 확인을 입력하세요.");
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            throw new IllegalArgumentException("변경할 비밀번호가 일치하지 않습니다.");
        }
    }

    public MemberInformationResponse findMemberInformation(Principal principal) {
        Member member = getMember(principal);
        return new MemberInformationResponse(member);
    }

    /**
     * 임시 비밀번호로 변경 이후 이메일 전송 메소드
     *
     * @param email
     * @param phoneNo
     * @return tempPassword
     */
    @Transactional
    public String changePasswordAndSendEmail(String email, String phoneNo) {
        /* --- 폼 검증 --- */
        validateEmail(email);
        validatePhoneNo(phoneNo);

        Member member = findMember(email, phoneNo);

        /* --- 임시 비밀번호 변경 --- */
        String tempPassword = createTempPassword();
        String encodedTempPassword = passwordEncoder.encode(tempPassword);
        member.updatePassword(encodedTempPassword);

        /* --- 이메일 전송 --- */
        mailService.send(email, tempPassword);
        return tempPassword;
    }

    @Transactional
    public boolean leave(Member member, String password) {
        /* --- 비밀번호 일치하는지 확인 --- */
        String encodedPassword = member.getPassword();
        matchPasswords(password, encodedPassword);

        /* Long memberId = member.getId();

         *//* --- 주문의 member null처리--- *//*
        List<Orders> orders = ordersRepository.findAllByMember(member);
        List<Long> orderIds = orders.stream()
                .map(order -> order.getId())
                .collect(Collectors.toList());*/

        ordersRepository.bulkMemberIdNull(member); //Order을 삭제하면 OrderBook 왜래키 참조하므로 에러

        /*--- 리뷰 조회 --- */
        List<Review> reviews = reviewRepository.findByMember(member);


        List<Long> reviewIds = reviews.stream()
                .map(r -> r.getId())
                .collect(Collectors.toList());

        //리뷰의 좋아요들 제거
        reviewLikeLogRepository.mDeleteAllByReviewsInQuery(reviews);

        //리뷰의 댓글들 제거
        reviewCommentRepository.mDeleteAllByReviewInQuery(reviews);

        //도서의 리뷰 건수 빼기
        List<Book> reviewedBooks = reviews.stream()
                .map(Review::getBook)
                .toList();
        bookRepository.updateReviewCountByBookIdInQuery(reviewedBooks);

        /*--- 이제 회원의 리뷰 제거 ---*/
        reviewRepository.mDeleteByMember(member);

        //*  --- 회원의 리뷰 댓글 제거 --- *//*
        reviewCommentRepository.mDeleteByMember(member);

        /*--- 회원이 좋아요 누른 리뷰 찾아서 좋아요 카운트 감소 ---*/
        List<ReviewLikeLog> reviewLikeLogs = reviewLikeLogRepository.findByMember(member);
        List<Review> likedReviews = reviewLikeLogs.stream().map(ReviewLikeLog::getReview).toList();
        reviewRepository.updateLikesCountByReviewInQuery(likedReviews);

        //* --- 회원의 좋아요 로그 제거 --- *//*
        reviewLikeLogRepository.mDeleteByMember(member);



        /*--- 회원의 구매목록 제거 ---*/
        libraryRepository.mDeleteByMember(member);

        memberRepository.delete(member);
        return true;
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().equals("")) {
            throw new IllegalArgumentException("이메일을 올바르게 입력해주세요.");
        }

        String[] splitEmail = email.split("@");
        if (splitEmail[0].length() < 4 || splitEmail[0].length() > 24) {
            throw new IllegalArgumentException("이메일을 올바르게 입력해주세요.");
        }
    }

    private String createTempPassword() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 8);
    }

    private String createConfirmSellCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 4);
    }

    private Member findMember(String email, String phoneNo) {
        return memberRepository.findByEmailAndPhoneNo(email, phoneNo)
                .orElseThrow(() -> new BaseException(BaseResponseCode.MEMBER_NOT_FOUND));
    }

    public int findTodayRegisterMemberCount() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfToday = today.with(LocalTime.MIN);
        LocalDateTime endOfToday = today.with(LocalTime.MAX);
        return memberRepository.countByCreatedTimeBetween(startOfToday, endOfToday);
    }
}