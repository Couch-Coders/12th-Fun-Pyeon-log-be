package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void createReview(ReviewDTO reviewDTO) {
        User user = userService.getUser(reviewDTO.getUserEmail());
        Review review = Review.builder()
                .reviewContent(reviewDTO.getReviewContent())
                .starCount(reviewDTO.getStarCount())
                .storeId(reviewDTO.getStoreId())
                .user(user)
                .keywords(new ArrayList<>())
                .build();

        reviewDTO.removeSameKeyword();
        review.addAllKeywords(keywordContentService.getAllKeywordContent(reviewDTO.getKeywords()));

        reviewRepository.save(review);
        storeService.addReviewInSummary(review);
    }

    @Transactional
    public void modifyReview(ReviewDTO reviewDTO) {
        Review review = getReview(reviewDTO.getReviewEntryNo());
        if (!review.getUser().getEmail().equals(reviewDTO.getUserEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바른 유저가 아닙니다!");

        Review oldReview = new Review(review);
        
        reviewDTO.removeSameKeyword();
        review.modifyReview(reviewDTO);

        keywordRepository.deleteByReview_ReviewEntryNo(review.getReviewEntryNo());


        review.initKeywords();
        review.addAllKeywords(keywordContentService.getAllKeywordContent(reviewDTO.getKeywords()));

        storeService.modifyReviewInSummary(review, oldReview);
    }

    @Transactional
    public void deleteReview(Long reviewEntryNo) {
        Review review = getReview(reviewEntryNo);
        reviewRepository.deleteById(reviewEntryNo);
        storeService.deleteReviewInSummary(review);
    }

    public Review getReview(Long reviewEntryNo){
        return reviewRepository.findById(reviewEntryNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다."));
    }

    private List<ReviewDTO> convertToReviewDTOS(List<Review> reviews) {
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review r : reviews) {
            reviewDTOS.add(new ReviewDTO(r));
        }
        return reviewDTOS;
    }
}
