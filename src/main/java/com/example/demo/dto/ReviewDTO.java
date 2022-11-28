package com.example.demo.dto;

import lombok.Data;

import java.util.Date;


@Data
public class ReviewDTO {
    Long reviewEntryNo;
    String reviewContent;
    int startCount;
    Date createdDate;
    Date modifiedDate;
    String storeId;

    public ReviewDTO() {
    }
}
