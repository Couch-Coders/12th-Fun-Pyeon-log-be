package com.example.demo.service;

import com.example.demo.dto.StoreSummaryDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoreService {

    ReviewRepository reviewRepository;
    StoreSummaryRepository storeSummaryRepository;
    KeywordRepository keywordRepository;
    KeywordContentRepository keywordContentRepository;
    StoreKeywordRepository storeKeywordRepository;
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
            return new StoreSummaryDTO(new StoreSummary(storeId));
        storeSummary.sortByKeywordCount();
        return new StoreSummaryDTO(storeSummary, 5);
    }

    public StoreSummary updateStoreSummary(String storeId) {
        List<Review> reviews = reviewRepository.findByStoreId(storeId);
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
        storeKeywordRepository.deleteByStoreSummary_StoreId(storeId);

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

    @Transactional
    public void addReviewInSummary(Review review){
        String storeId = review.getStoreId();
        StoreSummary summary = storeSummaryRepository.findById(storeId)
                .orElse(new StoreSummary(storeId));

        Long reviewCount = reviewRepository.countByStoreId(storeId).get();

        double starRate = reviewCount != 0 ?
                (summary.getStarRate() * (reviewCount-1) + review.getStarCount()) / (reviewCount) : 0;

        starRate = Math.round(starRate * 10);
        starRate /= 10;

        increaseStoreKeywordCounts(summary, review.getKeywords());
        StoreSummary updatedSummary = StoreSummary.builder()
                .storeId(storeId)
                .starRate(starRate)
                .reviewCount(reviewCount)
                .storeKeywords(summary.getStoreKeywords())
                .build();

        storeSummaryRepository.save(updatedSummary);
    }

    public void modifyReviewInSummary(Review newReview, Review oldReview){
        String storeId = newReview.getStoreId();
        StoreSummary summary = storeSummaryRepository.findById(storeId)
                .orElse(new StoreSummary(storeId));

        Long reviewCount = reviewRepository.countByStoreId(storeId).get();

        double gap = newReview.getStarCount() - oldReview.getStarCount();
        double starRate = reviewCount != 0 ?
                (summary.getStarRate() * (reviewCount) + gap) / (reviewCount) : 0;

        starRate = Math.round(starRate * 10) / 10;

        decreaseStoreKeywordCounts(summary, oldReview.getKeywords());
        increaseStoreKeywordCounts(summary, newReview.getKeywords());
        StoreSummary updatedSummary = StoreSummary.builder()
                .storeId(storeId)
                .starRate(starRate)
                .reviewCount(reviewCount)
                .storeKeywords(summary.getStoreKeywords())
                .build();

        storeSummaryRepository.save(updatedSummary);
    }

    public void deleteReviewInSummary(Review review){
        String storeId = review.getStoreId();
        StoreSummary summary = storeSummaryRepository.findById(storeId)
                .orElse(new StoreSummary(storeId));

        Long reviewCount = reviewRepository.countByStoreId(storeId).get();

        double starRate = reviewCount != 0 ?
                (summary.getStarRate() * (reviewCount+1) - review.getStarCount()) / (reviewCount) : 0;

        starRate = Math.round(starRate * 10) / 10;

        decreaseStoreKeywordCounts(summary, review.getKeywords());
        StoreSummary updatedSummary = StoreSummary.builder()
                .storeId(storeId)
                .starRate(starRate)
                .reviewCount(reviewCount)
                .storeKeywords(summary.getStoreKeywords())
                .build();

        storeSummaryRepository.save(updatedSummary);
    }

    private void increaseStoreKeywordCounts(StoreSummary summary, List<Keyword> keywords) {
        List<StoreKeyword> storeKeywords = summary.getStoreKeywords();

        Map<String, StoreKeyword> storeKeywordMap = new HashMap<>();
        for (StoreKeyword sk : storeKeywords)
            storeKeywordMap.put(sk.getKeywordContent().getKeywordContent(), sk);

        for (Keyword k : keywords) {
            String content = k.getKeywordContent().getKeywordContent();
            if (!storeKeywordMap.containsKey(content)) {
                storeKeywordMap.put(content, StoreKeyword.builder()
                        .keywordCount(1l)
                        .keywordContent(k.getKeywordContent())
                        .storeSummary(summary).build());
                storeKeywords.add(storeKeywordMap.get(content));
                continue;
            }
            storeKeywordMap.get(content).increaseKeywordCount();
        }
    }
}