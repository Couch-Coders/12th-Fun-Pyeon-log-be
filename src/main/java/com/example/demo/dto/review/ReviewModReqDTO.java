package com.example.demo.dto.review;

import lombok.Data;

import java.util.List;

@Data
public class ReviewModReqDTO {

    Long reviewEntryNo;

    String reviewContent;

    Double starCount;

    String userEmail;

    String storeId;

    List<String> keywords;

    public void removeSameKeyword() {
        this.keywords = this.keywords.stream().distinct().toList();
    }
}
