package com.example.demo.service;

import com.example.demo.dto.StoreSummaryDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class StoreService {
    StoreSummaryRepository storeSummaryRepository;

    public List<StoreSummaryDTO> getStoreSummaryDTOS(String[] storeIds) {
        List<StoreSummary> storeSummaries = getOrCreateStoreSummaries(storeIds);
        return convertStoreSummaryDTOS(storeSummaries);
    }

    public StoreSummaryDTO getStoreSummaryDTO(String storeId) {
        StoreSummary summary = getOrCreateStoreSummary(storeId);
        return new StoreSummaryDTO(summary, 5);
    }

    @Transactional
    public void addReviewInSummary(Review review){
        StoreSummary summary = getOrCreateStoreSummary(review.getStoreId());
        summary.addReview(review);
        storeSummaryRepository.save(summary);
    }

    @Transactional
    public void modifyReviewInSummary(Review newReview, Review oldReview){
        StoreSummary summary = getStoreSummary(newReview.getStoreId());
        summary.modifyReview(newReview, oldReview);
    }

    @Transactional
    public void deleteReviewInSummary(Review review){
        StoreSummary summary = getStoreSummary(review.getStoreId());
        summary.deleteReview(review);
    }

    public StoreSummary getStoreSummary(String storeId) throws ResponseStatusException {
        return storeSummaryRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "편의점이 존재하지 않습니다."));
    }

    public List<StoreSummary> getOrCreateStoreSummaries(String[] storeIds){
        List<StoreSummary> storeSummaries = storeSummaryRepository.findAllByStoreIdIn(storeIds);
        Map<String, StoreSummary> storeSummaryMap = new HashMap<>();
        for (StoreSummary summary : storeSummaries) {
            storeSummaryMap.put(summary.getStoreId(), summary);
        }

        for (String storeId : storeIds) {
            if (!storeSummaryMap.containsKey(storeId)) {
                storeSummaries.add(new StoreSummary(storeId));
            }
        }
        return storeSummaries;
    }

    private StoreSummary getOrCreateStoreSummary(String storeId) {
        StoreSummary summary;
        try {
            summary = getStoreSummary(storeId);
        } catch (ResponseStatusException e) {
            summary = createStoreSummary(storeId);
            storeSummaryRepository.save(summary);
            log.error(storeId + " is not exist, create store-summary : {}" + e.getMessage());
        }
        return summary;
    }

    private List<StoreSummaryDTO> convertStoreSummaryDTOS(List<StoreSummary> storeSummaries) {
        List<StoreSummaryDTO> storeSummaryDTOS = new ArrayList<>();
        for (StoreSummary storeSummary : storeSummaries) {
            storeSummary.sortByKeywordCount();
            storeSummaryDTOS.add(new StoreSummaryDTO(storeSummary));
        }
        return storeSummaryDTOS;
    }
}