package com.example.demo.dto;

import com.example.demo.entity.Keyword;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
public class ReviewDTO {
    Long reviewEntryNo;
    String reviewContent;
    Double starCount;
    LocalDateTime createdDate;
    LocalDateTime modifiedDate;
    Long userEntryNo;
    String storeId;
    List<String> keywords;

    public ReviewDTO() {
    }
}
