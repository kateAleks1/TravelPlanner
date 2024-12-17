package org.example.Controller;

import org.example.DTO.ReviewDto;
import org.example.DTO.TripDto;
import org.example.Service.ReviewService;
import org.example.entity.Review;
import org.example.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")

public class ReviewController {
    @Autowired
    private  ReviewService reviewService;



    @PostMapping("/createNewTrip")
    public ResponseEntity<?> createNewTrip(@RequestBody ReviewDto reviewDto) {

        Review review= reviewService.createReview(reviewDto);

        return ResponseEntity.ok(review);

    }
    @GetMapping("getAllReviews")
    public ResponseEntity<?> getAllReviews() {


        return ResponseEntity.ok(reviewService.getAllEReviews());

    }
}
