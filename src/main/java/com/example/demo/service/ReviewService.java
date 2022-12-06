package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Keyword;
import com.example.demo.entity.KeywordContent;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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

    public List<ReviewDTO> getReviews(String storeId, Pageable pageable) {
        List<Review> reviews = reviewRepository.findByStoreIdOrderByModifiedDateDesc(pageable, storeId);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review r : reviews)
            reviewDTOS.add(new ReviewDTO(r));
        return reviewDTOS;
    }
}
