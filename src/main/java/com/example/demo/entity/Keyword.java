package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Keyword {

    @Id
    @GeneratedValue
    Long keywordEntryNo;

    @Column
    String storeId;

    @OneToOne
    @JoinColumn(name = "keyword_no")
    KeywordContent keywordContent;

    @ManyToOne
    @JoinColumn(name = "user_entry_no")
    User user;

    @ManyToOne
    @JoinColumn(name = "review_entry_no")
    Review review;

}
