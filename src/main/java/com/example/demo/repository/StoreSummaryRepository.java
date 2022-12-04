package com.example.demo.repository;

import com.example.demo.entity.StoreSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreSummaryRepository extends JpaRepository<StoreSummary, String> {
    List<StoreSummary> findAllByStoreIdIn(String[] storeIds);
}
