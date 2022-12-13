package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class StoreKeyword {
    @Id @GeneratedValue
    Long storeKeywordNo;

    @ManyToOne
    @JoinColumn(name = "keyword_content_no")
    KeywordContent keywordContent;

    @ManyToOne
    @JoinColumn(name = "store_id")
    StoreSummary storeSummary;

    @Column
    Long keywordCount;

    @Builder
    public StoreKeyword(Long storeKeywordNo, KeywordContent keywordContent, StoreSummary storeSummary, Long keywordCount) {
        this.storeKeywordNo = storeKeywordNo;
        this.keywordContent = keywordContent;
        this.storeSummary = storeSummary;
        this.keywordCount = keywordCount;
    }

    public void increaseKeywordCount() {
        this.keywordCount++;
    }

    public void decreaseKeywordCount() {
        this.keywordCount--;
    }

    public StoreKeyword() {
    }
}
