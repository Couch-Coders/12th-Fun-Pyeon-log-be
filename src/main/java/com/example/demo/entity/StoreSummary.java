package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.*;

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

    public void addReview(Review review) {
        addStarCount(review.getStarCount());
        increaseStoreKeywordCounts(review.getKeywords());
    }
    public List<String> getKeywordContents(int keywordsSizeLimit){
        return storeKeywords.stream()
                .filter(storeKeyword -> storeKeyword.getKeywordCount() > 0)
                .map(storeKeyword -> storeKeyword.getKeywordContent().getKeywordContent())
                .limit(keywordsSizeLimit)
                .toList();
    }

    public List<String> getKeywordContents(){
        return storeKeywords.stream()
                        .filter(storeKeyword -> storeKeyword.getKeywordCount() > 0)
                        .map(storeKeyword -> storeKeyword.getKeywordContent().getKeywordContent())
                        .toList();
    }

    public void sortByKeywordCount(){
        Collections.sort(storeKeywords, (o1, o2) -> (int) -(o1.getKeywordCount() - o2.getKeywordCount()));
    }

    public void addStarCount(double starCount){
        double starRate = reviewCount != 0 ?
                (getStarRate() * (reviewCount) + starCount) / (reviewCount+1) : starCount;
        this.starRate = (double) Math.round(starRate * 10) / 10;
        this.reviewCount++;
    }

    public void modifyStarCount(double gap) {
        double starRate = (getStarRate() * reviewCount + gap) / reviewCount;
        this.starRate = (double) Math.round(starRate * 10) / 10;
    }

    public void deleteStarCount(double starCount) {
        double starRate = (getStarRate() * reviewCount - starCount) / (reviewCount-1);
        this.starRate = (double) Math.round(starRate * 10) / 10;
        this.reviewCount--;
    }


    public void increaseStoreKeywordCounts(List<Keyword> keywords) {
        List<StoreKeyword> storeKeywords = getStoreKeywords();

        Map<String, StoreKeyword> storeKeywordMap = new HashMap<>();
        for (StoreKeyword sk : storeKeywords)
            storeKeywordMap.put(sk.getKeywordContent().getKeywordContent(), sk);

        for (Keyword k : keywords) {
            String content = k.getKeywordContent().getKeywordContent();
            if (!storeKeywordMap.containsKey(content)) {
                storeKeywordMap.put(content, StoreKeyword.builder()
                        .keywordCount(1l)
                        .keywordContent(k.getKeywordContent())
                        .storeSummary(this).build());
                storeKeywords.add(storeKeywordMap.get(content));
                continue;
            }
            storeKeywordMap.get(content).increaseKeywordCount();
        }
    }
    public void decreaseStoreKeywordCounts(List<Keyword> keywords){
        List<StoreKeyword> storeKeywords = getStoreKeywords();

        Map<String, StoreKeyword> storeKeywordMap = new HashMap<>();
        for (StoreKeyword sk : storeKeywords)
            storeKeywordMap.put(sk.getKeywordContent().getKeywordContent(), sk);

        for (Keyword k : keywords) {
            String content = k.getKeywordContent().getKeywordContent();
            if (!storeKeywordMap.containsKey(content))
                continue;
            storeKeywordMap.get(content).decreaseKeywordCount();
        }
    }
}
