package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Keyword {

    @Id
    @GeneratedValue
    Long keywordEntryNo;

    @Column
    String storeId;

    @ManyToOne
    @JoinColumn(name = "keyword_no")
    KeywordContent keywordContent;

    @ManyToOne
    @JoinColumn(name = "user_entry_no")
    User user;

    @ManyToOne
    @JoinColumn(name = "review_entry_no")
    Review review;

    @Builder
    public Keyword(Long keywordEntryNo, String storeId, KeywordContent keywordContent, User user, Review review) {
        this.keywordEntryNo = keywordEntryNo;
        this.storeId = storeId;
        this.keywordContent = keywordContent;
        this.user = user;
        this.review = review;
    }

    public Keyword() {
    }
}
