package com.example.demo.dto;

import com.example.demo.entity.Keyword;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StoreDTO {
    String id;
    Double StarCount;
    Integer reviewCount;

    List<String> keywordList;

    @Builder
    public StoreDTO(String id, Double starCount, Integer reviewCount, List<String> keywordList) {
        this.id = id;
        this.StarCount = starCount;
        this.reviewCount = reviewCount;
        this.keywordList = keywordList;
    }

    public void setKeywordList(List<String> keywordList, int keywordsSizeLimit) {
        int keywordsSize = keywordList.size();
        int cutCount = keywordsSizeLimit > keywordsSize ? keywordsSize : keywordsSizeLimit;
        keywordList.subList(0, cutCount);
        this.keywordList = keywordList;
    }

    public StoreDTO() {
    }
}
