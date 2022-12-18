package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.review.ReviewCreationReqDTO;
import com.example.demo.dto.review.ReviewModReqDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {

    ReviewRepository reviewRepository;
    KeywordRepository keywordRepository;
    StoreService storeService;
    UserService userService;
    KeywordContentService keywordContentService;

    public List<ReviewDTO> getReviews(String storeId, Pageable pageable) {
        List<Review> reviews = reviewRepository.findByStoreId(pageable, storeId);
        return convertReviewDTOS(reviews);
    }

    @Transactional
    public void createReview(ReviewCreationReqDTO dto) {
        User user = userService.getUser(dto.getUserEmail());
        Review review = Review.builder()
                .reviewContent(dto.getReviewContent())
                .starCount(dto.getStarCount())
                .storeId(dto.getStoreId())
                .user(user)
                .keywords(new ArrayList<>())
                .build();

        reviewDTO.removeSameKeyword();
        review.initAllKeywords(keywordContentService.getAllKeywordContent(reviewDTO.getKeywords()));

        reviewRepository.save(review);
        storeService.addReviewInSummary(review);
    }

    @Transactional
    public void modifyReview(ReviewModReqDTO dto) {
        Review review = getReview(dto.getReviewEntryNo());
        if (!review.isSameUserEmail(dto.getUserEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바른 유저가 아닙니다!");

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바른 유저가 아닙니다!");
        reviewRepository.deleteById(reviewEntryNo);
        storeService.deleteReviewInSummary(review);
    }

    public Review getReview(Long reviewEntryNo) {
        return reviewRepository.findById(reviewEntryNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다."));
    }

    private List<ReviewDTO> convertReviewDTOS(List<Review> reviews) {
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review r : reviews) {
            reviewDTOS.add(new ReviewDTO(r));
        }
        return reviewDTOS;
    }
}
