package org.example.Service;

import org.example.DTO.ReviewDto;
import org.example.DTO.TripDto;
import org.example.entity.Destination;
import org.example.entity.Review;
import org.example.entity.Trip;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewService {
    Review createReview(ReviewDto ReviewDto);
    List<Review> getAllEReviews();
    Review updateReview(int destination, int userId,ReviewDto reviewDto);
    Review findReviewByDestinationIdAndUserId(int destination, int userId);
    int findReviewRatingByDestinationIdAndUserId(int destination, int userId);
    Double averageRatingFromDestinationId(int destinationId);
}
