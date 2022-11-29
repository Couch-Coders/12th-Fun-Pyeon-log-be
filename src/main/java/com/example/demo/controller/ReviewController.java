package com.example.demo.controller;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores/{storeId}/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

//    @GetMapping("")
//    public List<Review> getReviews(){
//        return reviewService.getReviews();
//    }

    @PostMapping("")
    public String addReview(@RequestBody ReviewDTO reviewDTO,
                            @PathVariable String storeId){
        reviewService.createReview(reviewDTO, storeId);
        return "";
    }

}
