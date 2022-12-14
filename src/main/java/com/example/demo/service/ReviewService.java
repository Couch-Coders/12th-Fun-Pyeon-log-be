package com.example.demo.service;

import com.example.demo.dto.review.ReviewCreationReqDTO;
import com.example.demo.dto.review.ReviewModReqDTO;
import com.example.demo.dto.review.ReviewRespDTO;
import com.example.demo.entity.*;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.*;
import com.example.demo.repository.keywordcontent.KeywordContentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    ReviewRepository reviewRepository;
    KeywordRepository keywordRepository;
    StoreService storeService;
    UserService userService;
    KeywordContentRepository keywordContentRepository;

    public List<ReviewRespDTO> getReviews(String storeId, Pageable pageable) {
        List<Review> reviews = reviewRepository.findByStoreIdOrderByCreatedDateDesc(pageable, storeId);
        return convertReviewResponseDTOS(reviews);
    }

    @Transactional
    public void createReview(ReviewCreationReqDTO dto, String userEmail) {
        User user = userService.getActiveUser(userEmail);
        Review review = Review.builder()
                .reviewContent(dto.getReviewContent())
                .starCount(dto.getStarCount())
                .storeId(dto.getStoreId())
                .user(user)
                .keywords(new ArrayList<>())
                .build();

        dto.removeSameKeyword();
        review.initAllKeywords(keywordContentRepository.getKeywordContentsByContent(dto.getKeywords()));

        reviewRepository.save(review);
        storeService.addReviewInSummary(review);
    }

    @Transactional
    public void modifyReview(ReviewModReqDTO dto) {
        Review review = getReview(dto.getReviewEntryNo());
        if (!review.isSameUserEmail(dto.getUserEmail()))
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "????????? ????????? ????????? ????????????!");

        Review oldReview = new Review(review);

        dto.removeSameKeyword();
        review.modifyReview(dto);

        keywordRepository.deleteByReview_ReviewEntryNo(review.getReviewEntryNo());
        review.initAllKeywords(keywordContentRepository.getKeywordContentsByContent(dto.getKeywords()));

        storeService.modifyReviewInSummary(review, oldReview);
    }

    @Transactional
    public void deleteReview(Long reviewEntryNo, String userEmail) {
        Review review = getReview(reviewEntryNo);
        if (!review.isSameUserEmail(userEmail))
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "????????? ????????? ????????? ????????????!");
        reviewRepository.deleteById(reviewEntryNo);
        storeService.deleteReviewInSummary(review);
    }

    public Review getReview(Long reviewEntryNo) {
        Review review = reviewRepository.findById(reviewEntryNo)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW, "????????? ???????????? ????????????."));
        if (!review.getUser().isActiveUser())
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "???????????? ???????????????. ?????? ????????? ????????????.");
        return review;
    }

    public List<ReviewRespDTO> convertReviewResponseDTOS(List<Review> reviews) {
        return reviews.stream()
                .sorted((o1, o2) -> o2.getCreatedDate().compareTo(o1.getCreatedDate()))
                .map(review -> new ReviewRespDTO(review))
                .toList();
    }
}
