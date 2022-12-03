package com.example.demo.service;

import com.example.demo.dto.StoreSummaryDTO;
import com.example.demo.entity.Keyword;
import com.example.demo.entity.Review;
import com.example.demo.entity.StoreSummary;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.StoreSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreService {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    StoreSummaryRepository storeSummaryRepository;

    public List<StoreSummaryDTO> getStoreSummaries(String[] storeIds) {
        List<StoreSummary> storeSummaries = storeSummaryRepository.findAllByStoreIdIn(storeIds);
        List<StoreSummaryDTO> storeSummaryDTOS = new ArrayList<>();

        for (StoreSummary storeSummary : storeSummaries) {
            storeSummary.sortByKeywordCount();
            StoreSummaryDTO storeSummaryDTO = StoreSummaryDTO.builder()
                    .storeId(storeSummary.getStoreId())
                    .reviewCount(storeSummary.getReviewCount())
                    .starCount(storeSummary.getStarRate())
                    .keywordList(storeSummary.getKeywordContents(3))
                    .build();
            storeSummaryDTOS.add(storeSummaryDTO);
        }

        return storeSummaryDTOS;
    }

    public List<String> getBestKeywordInReviewList(List<Review> reviewList) {
        Map<String, Integer> keywordMap = new HashMap<>();
        for (Review r : reviewList) {
            List<String> keywords = getKeywordListInReview(r);
            countKeyword(keywordMap, keywords);
        }

        List<String> bestKeywords = new ArrayList<>();
        for (String keyword : keywordMap.keySet())
            bestKeywords.add(keyword);

        Collections.sort(bestKeywords, (k1, k2) -> keywordMap.get(k2) - keywordMap.get(k1));
        return bestKeywords;
    }

    public List<String> getKeywordListInReview(Review review) {
        List<String> keywords = new ArrayList<>();
        for (Keyword k : review.getKeywords())
            keywords.add(k.getKeywordContent().getKeywordContent());
        return keywords;
    }

    public void countKeyword(Map<String, Integer> map, List<String> keywords) {
        for (String k : keywords) {
            if (!map.containsKey(k))
                map.put(k, 1);
            else
                map.put(k, map.get(k) + 1);
        }
    }

    public StoreSummaryDTO getStoreSummary(String storeId) {
        Optional<StoreSummary> optionalStoreSummary = storeSummaryRepository.findById(storeId);
        if (!optionalStoreSummary.isPresent())
            return StoreSummaryDTO.builder()
                    .storeId(storeId)
                    .build();

        StoreSummary storeSummary = optionalStoreSummary.get();
        storeSummary.sortByKeywordCount();

        StoreSummaryDTO storeSummaryDTO = StoreSummaryDTO.builder()
                .storeId(storeSummary.getStoreId())
                .reviewCount(storeSummary.getReviewCount())
                .starCount(storeSummary.getStarRate())
                .keywordList(storeSummary.getKeywordContents(3))
                .build();

        return storeSummaryDTO;
    }
}