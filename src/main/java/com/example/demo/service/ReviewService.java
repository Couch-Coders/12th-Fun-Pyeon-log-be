package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    KeywordRepository keywordRepository;
    @Autowired
    KeywordContentRepository keywordContentRepository;
    @Autowired
    StoreSummaryRepository storeSummaryRepository;

    @Autowired
    StoreKeywordRepository storeKeywordRepository;

    private Map<String, KeywordContent> allKeywordContentMap;

    @Transactional
    public void createReview(ReviewDTO reviewDTO, String storeId) {
        User user = userRepository.findById(reviewDTO.getUserEntryNo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다!"));

        Review review = Review.builder()
                .reviewContent(reviewDTO.getReviewContent())
                .starCount(reviewDTO.getStarCount())
                .storeId(storeId)
                .user(user)
                .keywords(new ArrayList<>())
                .build();
        reviewRepository.save(review);

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
        keywordRepository.saveAll(review.getKeywords());
        updateStoreSummary(review);
    }

    @Transactional
    public void updateStoreSummary(Review review) {
        String storeId = review.getStoreId();
        StoreSummary summary = storeSummaryRepository.findById(storeId)
                .orElse(new StoreSummary(storeId));

        List<Review> reviews = reviewRepository.findByStoreId(storeId);
        Long reviewCount = Long.valueOf(reviews.size());

        Double starAvr = reviews.stream()
                .mapToDouble(Review::getStarCount)
                .average()
                .orElse(0);

        StoreSummary updateSummary = StoreSummary.builder()
                .storeId(storeId)
                .starRate(starAvr)
                .reviewCount(reviewCount)
                .storeKeywords(summary.getStoreKeywords())
                .build();

        for (Keyword k : review.getKeywords()) {
            KeywordContent keywordContent = k.getKeywordContent();
            updateSummary.updateStoreKeyword(keywordContent);
        }
        storeSummaryRepository.save(updateSummary);
    }

    public List<ReviewDTO> getReviews(String storeId, Pageable pageable) {
        List<Review> reviews = reviewRepository.findByStoreIdOrderByCreatedDateDesc(pageable, storeId);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review r : reviews)
            reviewDTOS.add(new ReviewDTO(r));
        return reviewDTOS;
    }
    @Transactional
    public void modifyReview(String storeId, Long reviewEntryNo, ReviewDTO reviewDTO) {
        User user = userRepository.findById(reviewDTO.getUserEntryNo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다!"));

        Review review = reviewRepository.findById(reviewEntryNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 리뷰입니다!"));

        keywordRepository.deleteByStoreId(storeId);
        review.modifyReview(Review.builder()
                .reviewEntryNo(reviewEntryNo)
                .reviewContent(reviewDTO.getReviewContent())
                .starCount(reviewDTO.getStarCount())
                .storeId(storeId)
                .user(user)
                .keywords(new ArrayList<>())
                .build()
        );

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
    }

    private void setAllKeywordContents() {
        allKeywordContentMap = new HashMap<>();
        for (KeywordContent kc : keywordContentRepository.findAll())
            allKeywordContentMap.put(kc.getKeywordContent(), kc);
    }
}
