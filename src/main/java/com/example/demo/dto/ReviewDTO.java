package com.example.demo.dto;

import com.example.demo.entity.Keyword;
import com.example.demo.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class ReviewDTO {
    Long reviewEntryNo;
    String reviewContent;
    Double starCount;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;
    Long userEntryNo;
    String storeId;
    List<String> keywords;

    public ReviewDTO() {
    }

    public ReviewDTO(Review review) {
        this.reviewEntryNo = review.getReviewEntryNo();
        this.reviewContent = review.getReviewContent();
        this.starCount = review.getStarCount();
        this.createdDate = review.getCreatedDate();
        this.modifiedDate = review.getModifiedDate();
        this.userEntryNo = review.getUser().getUserEntryNo();
        this.storeId = review.getStoreId();
        castKeywordsToStrings(review.getKeywords());
    }

    public void castKeywordsToStrings(List<Keyword> keywords){
        if (keywords == null)
            return;
        if (this.keywords == null)
            this.keywords = new ArrayList<>();
        for (Keyword k : keywords)
            this.keywords.add(k.getKeywordContent().getKeywordContent());
    }

    public void removeSameKeyword() {
        Map<String, Boolean> keywordMap = new HashMap<>();
        List<String> newKeywords = new ArrayList<>();
        for (String k : this.keywords) {
            if (keywordMap.containsKey(k))
                continue;
            keywordMap.put(k, true);
            newKeywords.add(k);
        }
        this.keywords = newKeywords;
    }
}
