package org.example.Controller;

import org.example.DTO.ReviewDto;
import org.example.DTO.TripDto;
import org.example.Service.ReviewService;
import org.example.entity.Review;
import org.example.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/review")

public class ReviewController {
    @Autowired
    private  ReviewService reviewService;



    @PostMapping("/createNewReview")
    public ResponseEntity<?> createNewTrip(@RequestBody ReviewDto reviewDto) {

        Review review= reviewService.createReview(reviewDto);
        Map<String,Integer> map=Map.of("reviewId",review.getReviewId());
        return ResponseEntity.ok(map);

    }
    @GetMapping("findReviewByDestinationIdAndUserId/{destinationId}/{userId}")
    public ResponseEntity<?> findReviewByDestinationIdAndUserId(@PathVariable int destinationId,@PathVariable int userId) {
        return ResponseEntity.ok(reviewService.findReviewByDestinationIdAndUserId(destinationId,userId));

    }
    @GetMapping("averageRatingFromDestinationId/{destinationId}")
    public ResponseEntity<?> averageRatingFromDestinationId(@PathVariable int destinationId) {
        return ResponseEntity.ok(reviewService.averageRatingFromDestinationId(destinationId));

    }
    @PutMapping("update/{destinationId}/{userId}")
    public ResponseEntity<?> update(@PathVariable int destinationId,@PathVariable int userId,@RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.updateReview(destinationId,userId,reviewDto));

    }
    @GetMapping("findReviewRatingByDestinationIdAndUserId/{destinationId}/{userId}")
    public ResponseEntity<?> findReviewRatingByDestinationIdAndUserId(@PathVariable int destinationId,@PathVariable int userId) {
        return ResponseEntity.ok(reviewService.findReviewRatingByDestinationIdAndUserId(destinationId,userId));

    }

    @GetMapping("getAllReviews")
    public ResponseEntity<?> getAllReviews() {


        return ResponseEntity.ok(reviewService.getAllEReviews());

    }
}
