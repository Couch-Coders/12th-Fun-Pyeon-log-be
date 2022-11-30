package com.example.demo.service;

import com.example.demo.dto.StoreDTO;
import com.example.demo.entity.Keyword;
import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreService {

    @Autowired
    ReviewRepository reviewRepository;

    public List<StoreDTO> getStoreSummaries(String[] storeIds) {
        List<StoreDTO> storeDTOList = new ArrayList<>();
        for (String storeId : storeIds) {
            StoreDTO storeDTO = getStoreSummary(storeId);

            int sizeLimit = 3;
            int keywordCount = storeDTO.getKeywordList().size();

            storeDTO.setKeywordList(storeDTO.getKeywordList().subList(0,keywordCount));
            if (keywordCount >= sizeLimit)
                storeDTO.setKeywordList(storeDTO.getKeywordList().subList(0,sizeLimit));

            storeDTOList.add(storeDTO);
        }
        return storeDTOList;
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

    public void countKeyword(Map<String, Integer> map, List<String> keywords){
        for (String k : keywords) {
            if (!map.containsKey(k))
                map.put(k, 1);
            else
                map.put(k, map.get(k) + 1);
        }
    }

    public StoreDTO getStoreSummary(String storeId) {
        List<Review> reviewList = reviewRepository.findAllByStoreId(storeId);
        OptionalDouble averageOfStar = reviewList.stream()
                .mapToDouble(Review::getStarCount)
                .average();

        Double starCount = averageOfStar.isPresent() ? averageOfStar.getAsDouble() : 0.0;
        Integer reviewCount = reviewList.size();

        List<String> bestKeywords = getBestKeywordInReviewList(reviewList);

        return StoreDTO.builder()
                .id(storeId)
                .starCount(starCount)
                .reviewCount(reviewCount)
                .keywordList(bestKeywords)
                .build();
    }
}
