package com.example.demo.web.service;

import com.example.demo.web.domain.entity.Member;
import com.example.demo.web.domain.entity.Review;
import com.example.demo.web.domain.entity.ReviewLikeLog;
import com.example.demo.web.exception.BaseException;
import com.example.demo.web.exception.BaseResponseCode;
import com.example.demo.web.repository.ReviewLikeLogRepository;
import com.example.demo.web.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeService {

    private final ReviewLikeLogRepository reviewLikeLogRepository;
    private final ReviewRepository reviewRepository;

    /**
     *
     * @param member
     * @param reviewId
     */
    public void like(Member member, Long reviewId) {
        Review review = reviewRepository.getReferenceById(reviewId);

        /* --- 이미 좋아요를 눌렀는지 확인 --- */
        boolean result = reviewLikeLogRepository.existsByReviewAndMember(review, member);

        /* --- 좋아요 토글 --- */
        if(result){
            ReviewLikeLog log = reviewLikeLogRepository.findByReviewAndMember(review, member)
                    .orElseThrow(() -> new BaseException(BaseResponseCode.LIKE_NOT_FOUND));
            review.subtractLikesCount();
            reviewLikeLogRepository.delete(log);
        } else{
            ReviewLikeLog log = ReviewLikeLog.createReviewLikeLog(member, review);
            review.addLikesCount();
            reviewLikeLogRepository.save(log);
        }
    }
}