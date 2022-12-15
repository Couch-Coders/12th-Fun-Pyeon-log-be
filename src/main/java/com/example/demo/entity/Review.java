package com.example.demo.entity;

import com.example.demo.dto.ReviewDTO;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue
    Long reviewEntryNo;

    @Column
    String reviewContent;

    @Column
    Double starCount;

    @Column
    String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_entry_no")
    User user;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Keyword> keywords;

    @Builder
    public Review(Long reviewEntryNo, String reviewContent, Double starCount, String storeId, User user, List<Keyword> keywords) {
        this.reviewEntryNo = reviewEntryNo;
        this.reviewContent = reviewContent;
        this.starCount = starCount;
        this.storeId = storeId;
        this.user = user;
        this.keywords = keywords;
    }

    public Review() {
    }

    public void initKeywords() {
        this.keywords = new ArrayList<>();
    }

    public void modifyReview(Review review) {
        this.reviewEntryNo = review.getReviewEntryNo();
        this.reviewContent = review.getReviewContent();
        this.starCount = review.getStarCount();
        this.storeId = review.getStoreId();
        this.user = review.getUser();
        this.keywords = review.getKeywords();
    }

    public void initKeywords() {
        this.keywords = new ArrayList<>();
    }

    public void modifyReview(ReviewDTO reviewDTO) {
        this.reviewContent = reviewDTO.getReviewContent();
        this.starCount = reviewDTO.getStarCount();
        this.storeId = reviewDTO.getStoreId();
    }

    public void addAllKeywords(List<KeywordContent> allKeywordContent) {
        for (KeywordContent kc : allKeywordContent) {
            Keyword keyword = Keyword.builder()
                    .review(this)
                    .user(this.user)
                    .storeId(this.getStoreId())
                    .keywordContent(kc)
                    .build();
            this.getKeywords().add(keyword);
        }
    }
}
