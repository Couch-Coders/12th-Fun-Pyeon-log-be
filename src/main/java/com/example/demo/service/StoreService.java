package com.example.demo.service;

import com.example.demo.dto.StoreSummaryDTO;
import com.example.demo.entity.StoreSummary;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.StoreSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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