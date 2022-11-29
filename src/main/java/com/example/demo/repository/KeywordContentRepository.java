package com.example.demo.repository;

import com.example.demo.entity.KeywordContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordContentRepository extends JpaRepository<KeywordContent, Long> {
    Optional<KeywordContent> findByKeywordContent(String keywordContent);
}
