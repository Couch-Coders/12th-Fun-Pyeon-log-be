package com.example.demo.dto.review;

import com.example.demo.entity.Keyword;
import com.example.demo.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class ReviewRespDTO {
    Long reviewEntryNo;
    String reviewContent;
    Double starCount;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;
    String userEmail;
    String storeId;
    List<String> keywords;

    public ReviewRespDTO() {
    }

    public ReviewRespDTO(Review review) {
        this.reviewEntryNo = review.getReviewEntryNo();
        this.reviewContent = review.getReviewContent();
        this.starCount = review.getStarCount();
        this.createdDate = review.getCreatedDate();
        this.modifiedDate = review.getModifiedDate();
        this.userEmail = review.getUser().getEmail();
        this.storeId = review.getStoreId();
        castKeywordsToStrings(review.getKeywords());

        if (!review.getUser().isActiveUser())
            this.userEmail = "존재하지 않는 유저";
    }

    public void castKeywordsToStrings(List<Keyword> keywords){
        if (keywords == null)
            return;
        if (this.keywords == null)
            this.keywords = new ArrayList<>();
        for (Keyword k : keywords)
            this.keywords.add(k.getKeywordContent().getKeywordContent());
    }

}
