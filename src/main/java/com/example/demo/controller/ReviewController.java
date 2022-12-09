package com.example.demo.controller;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.entity.Review;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores/{storeId}/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("")
    public List<ReviewDTO> getReviews(@PathVariable String storeId, Pageable pageable){
        return reviewService.getReviews(storeId, pageable);
    }

    @PostMapping("")
    public ResponseEntity<String> addReview(@RequestBody ReviewDTO reviewDTO,
                                    @PathVariable String storeId){
        reviewService.createReview(reviewDTO, storeId);
        return ResponseEntity.ok().build();
    }

}
