package com.example.demo.service;


import com.example.demo.entity.KeywordContent;
import com.example.demo.repository.KeywordContentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class KeywordContentService {
    KeywordContentRepository keywordContentRepository;
    private Map<String, KeywordContent> allKeywordContentMap;

    private void setAllKeywordContents() {
        if (allKeywordContentMap == null)
            allKeywordContentMap = new HashMap<>();

        if (allKeywordContentMap.size() == 0)
            for (KeywordContent kc : keywordContentRepository.findAll())
                allKeywordContentMap.put(kc.getKeywordContent(), kc);
    }

    private KeywordContent getKeywordContent(String keywordContent){
        setAllKeywordContents();
        if (!this.allKeywordContentMap.containsKey(keywordContent))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 키워드입니다!");
        return this.allKeywordContentMap.get(keywordContent);
    }

    public List<KeywordContent> getAllKeywordContent(List<String> contents) {
        List<KeywordContent> keywordContents = new ArrayList<>();
        for (String content : contents)
            keywordContents.add(getKeywordContent(content));
        return keywordContents;
    }
}
