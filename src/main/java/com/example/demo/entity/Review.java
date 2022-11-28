package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
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
