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
    UserRepository userRepository;
    KeywordRepository keywordRepository;
    KeywordContentRepository keywordContentRepository;
    StoreService storeService;

    private Map<String, KeywordContent> allKeywordContentMap;

    @Transactional
    public void createReview(ReviewDTO reviewDTO, String storeId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다!"));

        Review review = Review.builder()
                .reviewContent(reviewDTO.getReviewContent())
                .starCount(reviewDTO.getStarCount())
                .storeId(storeId)
                .user(user)
                .keywords(new ArrayList<>())
                .build();

        reviewDTO.removeSameKeyword();
        for (String k : reviewDTO.getKeywords()) {
            KeywordContent keywordContent = keywordContentRepository.findByKeywordContent(k)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 키워드입니다!"));

            Keyword keyword = Keyword.builder()
                    .review(review)
                    .user(user)
                    .storeId(storeId)
                    .keywordContent(keywordContent)
                    .build();
            review.getKeywords().add(keyword);
        }
        reviewRepository.save(review);
        storeService.addReviewInSummary(review);
    }

    public List<ReviewDTO> getReviews(String storeId, Pageable pageable) {
        List<Review> reviews = reviewRepository.findByStoreId(pageable, storeId);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review r : reviews)
            reviewDTOS.add(new ReviewDTO(r));
        return reviewDTOS;
    }
    @Transactional
    public void modifyReview(String storeId, Long reviewEntryNo, ReviewDTO reviewDTO) {
        User user = userRepository.findByEmail(reviewDTO.getUserEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다!"));

        Review review = reviewRepository.findById(reviewEntryNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 리뷰입니다!"));

        Review oldReview = new Review();
        oldReview.modifyReview(review);

        keywordRepository.deleteByReview_ReviewEntryNo(review.getReviewEntryNo());
        review.modifyReview(Review.builder()
                .reviewEntryNo(reviewEntryNo)
                .reviewContent(reviewDTO.getReviewContent())
                .starCount(reviewDTO.getStarCount())
                .storeId(storeId)
                .user(user)
                .keywords(new ArrayList<>())
                .build());

        reviewDTO.removeSameKeyword();
        if (!isUsableKeywordContents(reviewDTO.getKeywords()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 키워드입니다!");

        for (String k : reviewDTO.getKeywords()) {
            Keyword keyword = Keyword.builder()
                    .review(review)
                    .user(user)
                    .storeId(storeId)
                    .keywordContent(getKeywordContent(k))
                    .build();
            review.getKeywords().add(keyword);
        }

        reviewRepository.save(review);
        storeService.modifyReviewInSummary(review, oldReview);
    }

    @Transactional
    public void deleteReview(String storeId, Long reviewEntryNo) {
        Review review = reviewRepository.findById(reviewEntryNo).orElse(null);
        if (review == null)
            return;

        reviewRepository.deleteById(reviewEntryNo);
        storeService.deleteReviewInSummary(review);
    }

    private void setAllKeywordContents() {
        allKeywordContentMap = new HashMap<>();
        for (KeywordContent kc : keywordContentRepository.findAll())
            allKeywordContentMap.put(kc.getKeywordContent(), kc);
    }

    private boolean isUsableKeywordContents(List<String> keywords) {
        setAllKeywordContents();
        for (String k : keywords)
            if (!this.allKeywordContentMap.containsKey(k))
                return false;
        return true;
    }

    private KeywordContent getKeywordContent(String keywordContent){
        return this.allKeywordContentMap.get(keywordContent);
    }
}
