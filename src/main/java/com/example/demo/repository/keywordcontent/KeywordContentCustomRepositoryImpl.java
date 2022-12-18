package com.example.demo.repository.keywordcontent;

import com.example.demo.entity.KeywordContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class KeywordContentCustomRepositoryImpl implements KeywordContentCustomRepository {

    @Autowired
    private EntityManager entityManager;

    private Map<String, KeywordContent> allKeywordContentMap;

    @Override
    public KeywordContent getKeywordContent(String keywordContent) {
        setAllKeywordContents();
        if (!this.allKeywordContentMap.containsKey(keywordContent))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 키워드입니다!");
        return this.allKeywordContentMap.get(keywordContent);
    }

    @Override
    public List<KeywordContent> getKeywordContentsByContent(List<String> contents) {
        setAllKeywordContents();
        List<KeywordContent> keywordContents = new ArrayList<>();
        for (String content : contents) {
            KeywordContent keywordContent = allKeywordContentMap.get(content);
            if (keywordContent == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 키워드입니다!");
            keywordContents.add(getKeywordContent(content));
        }
        return keywordContents;
    }

    private void setAllKeywordContents() {
        if (allKeywordContentMap == null)
            allKeywordContentMap = new HashMap<>();

        if (allKeywordContentMap.size() == 0)
            for (KeywordContent kc : findAll())
                allKeywordContentMap.put(kc.getKeywordContent(), kc);
    }

    private List<KeywordContent> findAll() {
        return (List<KeywordContent>) entityManager.createQuery("select kc from KeywordContent kc")
                .getResultList();
    }
}
