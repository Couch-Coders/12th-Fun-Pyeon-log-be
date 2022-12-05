package com.example.demo.service;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Keyword;
import com.example.demo.entity.KeywordContent;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.repository.KeywordContentRepository;
import com.example.demo.repository.KeywordRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    }

    public List<ReviewDTO> getReviews(String storeId) {
        List<Review> reviews = reviewRepository.findByStoreId(storeId);
        List<ReviewDTO> dtos = new ArrayList<>();
        for (Review r : reviews)
            dtos.add(new ReviewDTO(r));
        return dtos;
    }
}
