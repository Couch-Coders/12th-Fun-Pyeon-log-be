package com.example.demo.controller;

import com.example.demo.dto.review.ReviewCreationReqDTO;
import com.example.demo.dto.review.ReviewModReqDTO;
import com.example.demo.dto.review.ReviewRespDTO;
import com.example.demo.entity.User;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores/{storeId}/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("")
    public List<ReviewRespDTO> getReviews(@PathVariable String storeId, Pageable pageable){
        return reviewService.getReviews(storeId, pageable);
    }

    @PostMapping("")
    public ResponseEntity<String> addReview(@RequestBody ReviewCreationReqDTO creationDto,
                                            @PathVariable String storeId,
                                            @AuthenticationPrincipal User user){
        creationDto.setStoreId(storeId);
        creationDto.setUserEmail(user.getEmail());
        reviewService.createReview(creationDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reviewEntryNo}")
    public ResponseEntity<String> modifyReview(@PathVariable String storeId,
                                               @PathVariable Long reviewEntryNo,
                                               @RequestBody ReviewModReqDTO modDto,
                                               @AuthenticationPrincipal User user){
        modDto.setReviewEntryNo(reviewEntryNo);
        modDto.setStoreId(storeId);
        modDto.setUserEmail(user.getEmail());
        reviewService.modifyReview(modDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewEntryNo}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewEntryNo,
                                               @AuthenticationPrincipal User user){
        reviewService.deleteReview(reviewEntryNo, user.getEmail());
        return ResponseEntity.ok().build();
    }
}
