package org.example.Service;

import org.example.DTO.ReviewDto;
import org.example.DTO.TripDto;
import org.example.entity.Review;
import org.example.entity.Trip;

import java.util.List;

public interface ReviewService {
    Review createReview(ReviewDto ReviewDto);
    List<Review> getAllEReviews();
}
