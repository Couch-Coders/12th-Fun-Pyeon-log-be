package com.example.demo.service;

import com.example.demo.dto.StoreSummaryDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.KeywordContentRepository;
import com.example.demo.repository.KeywordRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.StoreSummaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoreService {

    ReviewRepository reviewRepository;
    StoreSummaryRepository storeSummaryRepository;
    KeywordRepository keywordRepository;
    KeywordContentRepository keywordContentRepository;
    Map<String, KeywordContent> keywordContentMap;

    public List<StoreSummaryDTO> getStoreSummaries(String[] storeIds) {
        List<StoreSummary> storeSummaries = storeSummaryRepository.findAllByStoreIdIn(storeIds);
        List<StoreSummaryDTO> storeSummaryDTOS = new ArrayList<>();

        for (StoreSummary storeSummary : storeSummaries) {
            storeSummary.sortByKeywordCount();
            storeSummaryDTOS.add(new StoreSummaryDTO(storeSummary));
        }

        return storeSummaryDTOS;
    }

    public StoreSummaryDTO getStoreSummary(String storeId) {
        StoreSummary storeSummary = storeSummaryRepository.findById(storeId)
                .orElse(null);
        if (storeSummary == null)
            return null;
        storeSummary.sortByKeywordCount();
        return new StoreSummaryDTO(storeSummary, 5);
    }

    public StoreSummary updateStoreSummary(String storeId) {
        List<Review> reviews = reviewRepository.findAllByStoreId(storeId);
        Long reviewCount = Long.valueOf(reviews.size());

        Double starRate = reviews.stream()
                .mapToDouble(Review::getStarCount)
                .average()
                .orElse(0.0);

        List<StoreKeyword> updateStoreKeyword = new ArrayList<>();

        StoreSummary updateSummary = StoreSummary.builder()
                .storeId(storeId)
                .reviewCount(reviewCount)
                .starRate(starRate)
                .storeKeywords(updateStoreKeyword)
                .build();

        updateStoreKeywords(updateSummary);
        return storeSummaryRepository.save(updateSummary);
    }

    public void updateStoreKeywords(StoreSummary summary) {
        String storeId = summary.getStoreId();
        List<Keyword> keywords = keywordRepository.findByStoreId(storeId);
        Map<String, Long> keywordMap = new HashMap<>();
        for (Keyword k : keywords) {
            String keywordContent = k.getKeywordContent().getKeywordContent();
            if (!keywordMap.containsKey(keywordContent))
                keywordMap.put(keywordContent, 0l);
            keywordMap.put(keywordContent, keywordMap.get(keywordContent)+1);
        }

        for (String k : keywordMap.keySet()) {
            summary.getStoreKeywords().add(
                    StoreKeyword.builder()
                    .storeSummary(summary)
                    .keywordContent(getKeywordContent(k))
                    .keywordCount(keywordMap.get(k))
                    .build()
            );
        }
    }
    private void initKeywordContentMap(){
        if (keywordContentMap == null)
            keywordContentMap = new HashMap<>();

        if (keywordContentMap.size() != 0)
            return;

        List<KeywordContent> keywordContents = keywordContentRepository.findAll();
        for (KeywordContent kc : keywordContents)
            keywordContentMap.put(kc.getKeywordContent(), kc);
    }

    private KeywordContent getKeywordContent(String content) {
        initKeywordContentMap();
        return keywordContentMap.get(content);
    }
}