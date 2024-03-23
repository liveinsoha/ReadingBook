package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Book;
import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.dto.response.MyWroteReviewInBookResponse;
import com.example.demo.web.dto.response.MyWroteReviewResponse;
import com.example.demo.web.dto.response.ReviewResponse;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.BookRepository;
import com.example.demo.web.repository.LibraryRepository;
import com.example.demo.web.repository.MemberRepository;
import com.example.demo.web.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LibraryRepository libraryRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    /**
     * 리뷰 작성 메소드
     *
     * @param member
     * @param book
     * @param content
     * @param starRating
     * @return reviewId
     */
    @Transactional
    public Long review(Member member, Book book, String content, int starRating) {
        /* --- 폼 검증 --- */
        validateForm(content, starRating);

        /* --- 이미 해당 도서에 리뷰를 작성했는지 검증 --- */
        validateMemberWroteReviewInSameBook(member.getId(), book.getId());

        /* --- 리뷰 작성자가 이 책을 구매 작성했는지 확인 --- */
        boolean isPurchased = libraryRepository.existsByBookAndMember(book, member);
        if (!isPurchased) {
            throw new BaseException(BaseResponseCode.REVIEW_FOR_PURCHASED_BOOK_ONLY);
        }
        /* --- 리뷰 카운트 추가 --- */
        book.addReviewCount();

        Review review = Review.createReview(member, book, content, starRating, isPurchased);
        return reviewRepository.save(review).getId();
    }

    private void validateMemberWroteReviewInSameBook(Long memberId, Long bookId) {
        boolean exists = reviewRepository.existsByMemberIdAndBookId(memberId, bookId);
        if (exists) {
            throw new BaseException(BaseResponseCode.ALREADY_REVIEWED);
        }
    }

    private void validateForm(String content, int starRating) {
        validateContent(content);

        if (starRating > 5 || starRating < 1) {
            throw new IllegalArgumentException("별점은 1에서 5의 정수만 올 수 있습니다.");
        }
    }


    private void validateContent(String content) {

        if (content == null || content.trim() == "") {
            throw new IllegalArgumentException("리뷰를 남겨주세요.");
        }

        if (content.length() < 10) {
            throw new IllegalArgumentException("10자 이상의 리뷰를 남겨주세요.");
        }

        if (content.length() > 2000) {
            throw new IllegalArgumentException("2000자 미만의 리뷰를 남겨주세요.");
        }
    }

    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.REVIEW_ID_NOT_FOUND));
    }


    public Review findReview(Long memberId, Long bookId) {
        Member member = memberRepository.getReferenceById(memberId);
        Book book = bookRepository.getReferenceById(bookId);
        return reviewRepository.findByMemberAndBook(member, book)
                .orElseThrow(() -> new BaseException(BaseResponseCode.REVIEW_NOT_FOUND));
    }

    /**
     * 본인이 작성한 모든 리뷰들 가져오는 메소드
     *
     * @param member
     * @return
     */
    public List<MyWroteReviewResponse> findWroteReviews(Member member) {
        return reviewRepository.findAllByMember(member).stream()
                .map(MyWroteReviewResponse::new)
                .collect(Collectors.toList());
    }


    /**
     * 도서에서 내가 작성한 리뷰 반환해주는 메소드
     *
     * @param memberId
     * @param isbn
     * @return MyWroteReviewResponse DTO
     */
    public MyWroteReviewInBookResponse findWroteReview(Long memberId, String isbn) {
        Book book = bookService.findBook(isbn);
        Long bookId = book.getId();

        Review review = null;
        try {
            review = findReview(memberId, bookId);
        } catch (BaseException e) {
            return null; //리뷰가 없는 경우 null 리턴
        }
        MyWroteReviewInBookResponse response = new MyWroteReviewInBookResponse(review);
        return response;
    }

    @Transactional
    public void update(Member member, Long reviewId, String content, int starRating) {
        Long memberId = member.getId();
        Review review = findReview(reviewId);

        /* --- 수정하고자 하는 리뷰가 본인이 작성한 리뷰인지 검증 --- */
        validateReviewIdentification(review, memberId);

        /* --- 폼 검증 --- */
        validateForm(content, starRating);

        review.update(content, starRating); // 변경 감지
    }

    private void validateReviewIdentification(Review review, Long memberId) {
        Long findMemberId = review.getMember().getId();
        if (findMemberId != memberId) {
            throw new BaseException(BaseResponseCode.ONLY_OWN_REVIEW_MODIFIABLE);
        }
    }

    /**
     * 리뷰 삭제 메소드
     *
     * @param member
     * @param reviewId
     */
    /**
     * 리뷰 제거 메소드
     * @param reviews
     */
    @Transactional
    public void deleteAll(List<Review> reviews) {
        reviewRepository.deleteAll(reviews);
    }


//    public int findTodayReviewCount() {
//        LocalDateTime today = LocalDateTime.now();
//        LocalDateTime startOfToday = today.with(LocalTime.MIN);
//        LocalDateTime endOfToday = today.with(LocalTime.MAX);
//
//        return reviewRepository.countByCreatedTimeBetween(startOfToday, endOfToday);
//    }



    @Transactional
    public boolean delete(Member member, Long reviewId) {
        Review review = findReview(reviewId);
        Long memberId = member.getId();

        /* --- 삭제하고자 하는 리뷰가 본인이 작성한 리뷰인지 검증 --- */
        validateReviewIdentification(review, memberId);

        /* --- 도서의 리뷰 수량 차감 --- */
        Book book = review.getBook();
        book.subtractReviewCount();

        reviewRepository.delete(review);
        return true;
    }

    public List<ReviewResponse> findReviews(Long bookId) {

        List<Review> reviews = reviewRepository.findReviewsByBookId(bookId);

        log.info("대댓글 in쿼리 로드 ============시작===========");
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
        log.info("대댓글 in쿼리 로드 ============끝===========");

        return reviewResponses;
    }

    public List<ReviewResponse> findReviews(String isbn) {

        Book book = bookService.findBook(isbn);
        List<Review> reviews = reviewRepository.findReviewsByBookId(book.getId());

        log.info("대댓글 in쿼리 로드 ============시작===========");
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
        log.info("대댓글 in쿼리 로드 ============끝===========");

        return reviewResponses;
    }
}