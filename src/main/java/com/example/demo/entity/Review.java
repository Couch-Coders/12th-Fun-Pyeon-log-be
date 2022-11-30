package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue
    Long reviewEntryNo;

    @Column
    String reviewContent;

    @Column
    Double starCount;

    @Column
    String storeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_entry_no")
    User user;

    @OneToMany(mappedBy = "review")
    List<Keyword> keywords;

    @Builder
    public Review(Long reviewEntryNo, String reviewContent, Double starCount, String storeId, User user, List<Keyword> keywords) {
        this.reviewEntryNo = reviewEntryNo;
        this.reviewContent = reviewContent;
        this.starCount = starCount;
        this.storeId = storeId;
        this.user = user;
        this.keywords = keywords;
    }

    public Review() {
    }
}
