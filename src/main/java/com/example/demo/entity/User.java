package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "users")
@Getter
@ToString
public class User {
    @Id
    @GeneratedValue
    Long userEntryNo;

    @Column
    String email;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    Date registeredDate;

    @Builder
    public User(Long userEntryNo, String email, Date registeredDate) {
        this.userEntryNo = userEntryNo;
        this.email = email;
        this.registeredDate = registeredDate;
    }

    public User() {

    }

}
