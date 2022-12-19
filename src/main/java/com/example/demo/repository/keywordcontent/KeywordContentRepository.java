package com.example.demo.repository.keywordcontent;

import com.example.demo.entity.KeywordContent;
import com.example.demo.repository.keywordcontent.KeywordContentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordContentRepository extends KeywordContentBasicRepository, KeywordContentCustomRepository {
    Optional<KeywordContent> findByKeywordContent(String keywordContent);
}
