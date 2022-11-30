package com.example.demo.service;

import com.example.demo.dto.StoreDTO;
import com.example.demo.entity.Keyword;
import com.example.demo.entity.Review;
import com.example.demo.repository.KeywordRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class StoreService {

    @Autowired
    ReviewRepository reviewRepository;

    public List<StoreDTO> getStoreSummaries(String[] storeIds) {
        List<StoreDTO> storeDTOList = new ArrayList<>();

        for (String storeId : storeIds) {
            List<Review> reviewList = reviewRepository.findAllByStoreId(storeId);
            if (reviewList == null)
                continue;

            Double starAverage = reviewList.stream()
                    .mapToDouble(Review::getStarCount)
                    .average()
                    .getAsDouble();

            Integer reviewCount = reviewList.size();

            List<String> bestKeywords;
            bestKeywords = getBestKeywordInReviewList(reviewList);

            StoreDTO storeDTO = StoreDTO.builder()
                    .id(storeId)
                    .starCount(starAverage)
                    .reviewCount(reviewCount)
                    .keywordList(bestKeywords)
                    .build();

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

        Collections.sort(bestKeywords, Comparator.comparingInt(keywordMap::get));
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


}
