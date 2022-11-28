package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class KeywordContent {
    @Id @GeneratedValue
    int keywordNo;
    String keywordContent;

    @Builder
    public KeywordContent(int keywordNo, String keywordContent) {
        this.keywordNo = keywordNo;
        this.keywordContent = keywordContent;
    }

    public KeywordContent() {
    }
}
