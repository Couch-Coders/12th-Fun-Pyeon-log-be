package com.example.demo.repository;

import com.example.demo.entity.KeywordContent;
import com.example.demo.entity.StoreKeyword;
import com.example.demo.entity.StoreSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreKeywordRepository extends JpaRepository<StoreKeyword, Long> {
    Optional<StoreKeyword> findByKeywordContentAndStoreSummary_StoreId(KeywordContent keywordContent, String storeId);

    List<StoreKeyword> findByStoreSummary_StoreId(String storeId);

    void deleteByStoreSummary_StoreId(String storeId);
}
