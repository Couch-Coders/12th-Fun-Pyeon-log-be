package com.example.demo.service;

import com.example.demo.dto.StoreSummaryDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class StoreService {
    StoreSummaryRepository storeSummaryRepository;

    public List<StoreSummaryDTO> getStoreSummaryDTOS(String[] storeIds) {
        List<StoreSummary> storeSummaries = storeSummaryRepository.findAllByStoreIdIn(storeIds);
        List<StoreSummaryDTO> storeSummaryDTOS = convertStoreSummaryDTOS(storeSummaries);

        return storeSummaryDTOS;
    }

    private List<StoreSummaryDTO> convertStoreSummaryDTOS(List<StoreSummary> storeSummaries) {
        List<StoreSummaryDTO> storeSummaryDTOS = new ArrayList<>();
        for (StoreSummary storeSummary : storeSummaries) {
            storeSummary.sortByKeywordCount();
            storeSummaryDTOS.add(new StoreSummaryDTO(storeSummary));
        }
        return storeSummaryDTOS;
    }

    public StoreSummaryDTO getStoreSummaryDTO(String storeId) {
        StoreSummary storeSummary = storeSummaryRepository.findById(storeId)
                .orElse(new StoreSummary(storeId));
        storeSummary.sortByKeywordCount();
        return new StoreSummaryDTO(storeSummary, 5);
    }

    @Transactional
    public void addReviewInSummary(Review review){
        StoreSummary summary = getStoreSummary(review.getStoreId());
        summary.addStarCount(review.getStarCount());

        summary.increaseStoreKeywordCounts(review.getKeywords());
        storeSummaryRepository.save(summary);
    }

    @Transactional
    public void modifyReviewInSummary(Review newReview, Review oldReview){
        StoreSummary summary = getStoreSummary(newReview.getStoreId());
        if (summary == null)
            return;

        double gap = newReview.getStarCount() - oldReview.getStarCount();
        summary.modifyStarCount(gap);
        summary.decreaseStoreKeywordCounts(oldReview.getKeywords());
        summary.increaseStoreKeywordCounts(newReview.getKeywords());
    }

    @Transactional
    public void deleteReviewInSummary(Review review){
        String storeId = review.getStoreId();
        StoreSummary summary = storeSummaryRepository.findById(storeId).orElse(null);
        if (summary == null)
            return;

        summary.deleteStarCount(review.getStarCount());
        summary.decreaseStoreKeywordCounts(review.getKeywords());
    }

    public StoreSummary getStoreSummary(String storeId) {
        return storeSummaryRepository.findById(storeId)
                .orElse(new StoreSummary(storeId));
    }
}