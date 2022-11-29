package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
public class Review {
    @Id
    @GeneratedValue
    Long reviewEntryNo;

    @Column
    String reviewContent;

    @Column
    int startCount;

    @Column
    Date createdDate;

    @Column
    Date modifiedDate;

    @Column
    String storeId;

    @ManyToOne
    @JoinColumn(name = "user_entry_no")
    User user;

}
