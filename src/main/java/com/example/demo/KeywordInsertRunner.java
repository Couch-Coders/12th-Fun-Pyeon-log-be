package com.example.demo;

import com.example.demo.entity.KeywordContent;
import com.example.demo.repository.KeywordContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeywordInsertRunner implements ApplicationRunner {

    @Autowired
    KeywordContentRepository keywordContentRepository;

    List<String> keywords;

    public KeywordInsertRunner() {
        keywords = new ArrayList<>();
        keywords.add("분위기가 좋아요");
        keywords.add("매장이 넓어요");
        keywords.add("접근성이 좋아요");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (int i=0 ; i<keywords.size() ; i++) {
            KeywordContent keywordContent = KeywordContent.builder()
                    .keywordNo(i+1)
                    .keywordContent(keywords.get(i))
                    .build();

            keywordContentRepository.save(keywordContent);
        }

    }

}
