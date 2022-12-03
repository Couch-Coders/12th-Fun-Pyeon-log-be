package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class StoreSummaryDTO {
    String storeId;
    Double StarCount;
    Long reviewCount;
    List<String> keywordList;

    @Builder
    public StoreSummaryDTO(String storeId, Double starCount, Long reviewCount, List<String> keywordList) {
        this.storeId = storeId;
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

    public StoreSummaryDTO() {
    }
}
