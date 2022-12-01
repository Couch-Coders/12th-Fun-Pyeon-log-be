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

    public ReviewDTO createReview(ReviewDTO reviewDTO, String storeId) {
        Optional<User> optionalUser = userRepository.findById(reviewDTO.getUserEntryNo());
        if (!optionalUser.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다!");

        Review review = Review.builder()
                .reviewContent(reviewDTO.getReviewContent())
                .starCount(reviewDTO.getStarCount())
                .storeId(storeId)
                .user(optionalUser.get())
                .keywords(new ArrayList<>())
                .build();

        for (String k : reviewDTO.getKeywords()) {
            Optional<KeywordContent> optionalContent = keywordContentRepository.findByKeywordContent(k);
            if (!optionalContent.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 키워드입니다!");

            KeywordContent keywordContent = optionalContent.get();
            Keyword keyword = Keyword.builder()
                    .review(review)
                    .user(optionalUser.get())
                    .storeId(storeId)
                    .keywordContent(keywordContent)
                    .build();
            review.getKeywords().add(keyword);
        }

        reviewRepository.save(review);
        keywordRepository.saveAll(review.getKeywords());

        return reviewDTO;
    }

    public List<Review> getReviews(String storeId) {
        List<Review> reviewList = reviewRepository.findByStoreId(storeId);
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review r : reviewList) {
            reviewDTOS.add(ReviewDTO)
        }

        return null;
    }
}
