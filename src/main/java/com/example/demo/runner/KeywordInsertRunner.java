package com.example.demo.runner;

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
        keywords.add("제품이 다양해요");
        keywords.add("트렌디한 상품이 많아요");
        keywords.add("자체상품이 좋아요");
        keywords.add("커피머신이 있어요");
        keywords.add("행사상품이 다양해요");
        keywords.add("직원이 친절해요");
        keywords.add("매장이 청결해요");
        keywords.add("뷰가 좋아요");
        keywords.add("혼밥하기 좋아요");
        keywords.add("매장이 넓어요");
        keywords.add("접근성이 좋아요");
        keywords.add("테이블이 많아요");
        keywords.add("택배이용이 편리해요");
        keywords.add("취식공간이 잘 되어있어요");
        keywords.add("주차하기 편해요");
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
