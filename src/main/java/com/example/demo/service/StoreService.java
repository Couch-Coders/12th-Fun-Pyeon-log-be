package com.example.demo.service;

import com.example.demo.dto.StoreSummaryDTO;
import com.example.demo.entity.StoreSummary;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.StoreSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            storeSummaryDTOS.add(new StoreSummaryDTO(storeSummary));
        }

        return storeSummaryDTOS;
    }

    public StoreSummaryDTO getStoreSummary(String storeId) {
        StoreSummary storeSummary = storeSummaryRepository.findById(storeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 편의점입니다."));
        storeSummary.sortByKeywordCount();
        return new StoreSummaryDTO(storeSummary);
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
}