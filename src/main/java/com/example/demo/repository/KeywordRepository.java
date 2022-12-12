package com.example.demo.repository;

import com.example.demo.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    void deleteByStoreId(String storeId);

    List<Keyword> findByStoreId(String storeId);
}
