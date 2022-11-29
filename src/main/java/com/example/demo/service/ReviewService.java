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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");

        Review review = Review.builder()
                .reviewContent(reviewDTO.getReviewContent())
                .starCount(reviewDTO.getStarCount())
                .storeId(storeId)
                .user(optionalUser.get())
                .keywords(new ArrayList<>())
                .build();

        reviewRepository.save(review);

        for (String msg : reviewDTO.getKeywords()) {
            Optional<KeywordContent> optionalKeywordContent = keywordContentRepository.findByKeywordContent(msg);
            if (!optionalKeywordContent.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 키워드입니다.");

            Keyword keyword = Keyword.builder()
                    .keywordContent(optionalKeywordContent.get())
                    .storeId(storeId)
                    .user(optionalUser.get())
                    .review(review)
                    .build();

            review.getKeywords().add(keyword);
            keywordRepository.save(keyword);
        }

        reviewDTO.setReviewEntryNo(review.getReviewEntryNo());
        reviewDTO.setStoreId(storeId);
        reviewDTO.setCreatedDate(review.getCreatedDate());
        reviewDTO.setModifiedDate(review.getModifiedDate());

        return reviewDTO;
    }
}
