package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;


@Data
public class ReviewDTO {
    Long reviewEntryNo;
    String reviewContent;
    Integer startCount;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;
    String storeId;

    public ReviewDTO() {
    }
}
