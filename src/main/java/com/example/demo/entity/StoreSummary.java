package com.example.demo.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class StoreSummary {

    @Id @GeneratedValue
    Integer storeId;

    @Column
    Double starRate;

    @Column
    Long reviewCount;

    @OneToMany(mappedBy = "store")
    List<Keyword> keywords;

}
