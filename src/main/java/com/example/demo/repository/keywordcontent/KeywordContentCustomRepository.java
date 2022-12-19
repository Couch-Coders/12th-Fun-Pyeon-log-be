package com.example.demo.repository.keywordcontent;

import com.example.demo.entity.KeywordContent;

import java.util.List;

public interface KeywordContentCustomRepository {

    KeywordContent getKeywordContent(String keywordContent);

    List<KeywordContent> getKeywordContentsByContent(List<String> contents);

}
