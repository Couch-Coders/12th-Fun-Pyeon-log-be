package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
public class StoreSummary {

    @Id
    String storeId;

    @Column
    Double starRate;

    @Column
    Long reviewCount;

    @OneToMany(mappedBy = "storeSummary", cascade = CascadeType.ALL)
    List<StoreKeyword> storeKeywords;

    @Builder
    public StoreSummary(String storeId, Double starRate, Long reviewCount, List<StoreKeyword> storeKeywords) {
        this.storeId = storeId;
        this.starRate = starRate;
        this.reviewCount = reviewCount;
        this.storeKeywords = storeKeywords;
    }

    public StoreSummary(String storeId) {
        this.storeId = storeId;
        this.starRate = 0.0;
        this.reviewCount = 0l;
        this.storeKeywords = new ArrayList<>();
    }

    public StoreSummary() {
    }

    public void updateStoreKeyword(KeywordContent keywordContent) {
        for (StoreKeyword sk : this.storeKeywords) {
            if (sk.getKeywordContent().equals(keywordContent)) {
                sk.increaseKeywordCount();
                return;
            }
        }

        this.storeKeywords.add(StoreKeyword.builder()
                .keywordContent(keywordContent)
                .storeSummary(this)
                .keywordCount(0l)
                .build()
        );
    }

    public List<String> getKeywordContents(int keywordsSizeLimit){
        List<String> keywords = new ArrayList<>();
        if (storeKeywords.size() >= keywordsSizeLimit)
            storeKeywords = storeKeywords.subList(0, keywordsSizeLimit);
        for (int i=0 ; i<storeKeywords.size() ; i++)
            keywords.add(storeKeywords.get(i).getKeywordContent().getKeywordContent());
        return keywords;
    }

    public void sortByKeywordCount(){
        Collections.sort(storeKeywords, (o1, o2) -> (int) -(o1.getKeywordCount() - o2.getKeywordCount()));
    }

}
