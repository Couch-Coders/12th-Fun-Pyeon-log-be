package com.example.demo.dto.review;

import lombok.Data;

import java.util.List;

@Data
public class ReviewCreationReqDTO {

    String reviewContent;

    Double starCount;

    String storeId;

    List<String> keywords;

    public void removeSameKeyword() {
        this.keywords = this.keywords.stream().distinct().toList();
    }

}
