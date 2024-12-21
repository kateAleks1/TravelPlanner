package org.example.Service.ServiceImpl;

import org.example.DTO.ReviewDto;
import org.example.Dal.Repository.DestinationsRepository;
import org.example.Dal.Repository.ReviewRepository;
import org.example.Dal.Repository.UserRepository;
import org.example.Service.ReviewService;
import org.example.entity.Destination;
import org.example.entity.Review;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private DestinationsRepository destinationsRepository;

    @Override
    public Review findReviewByDestinationIdAndUserId(int destinationId, int userId) {
        return reviewRepository.findReviewByDestinationIdAndUserId(destinationId,userId).get();
    }

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, DestinationsRepository destinationsRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.destinationsRepository = destinationsRepository;
    }

    @Override
    public Review createReview(ReviewDto reviewDto) {

        Review review = new Review();

        review.setReviewDesc(reviewDto.getReviewDesc());
        review.setCreatingDate(reviewDto.getCreatingDate());
        review.setReviewRating(reviewDto.getReviewRating());

        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        review.setUser(user);

        Destination destination = destinationsRepository.findById(reviewDto.getDestinationId())
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        review.setDestination(destination);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllEReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review updateReview(int destinationId, int userId, ReviewDto reviewDto) {
        Review review = reviewRepository.findReviewByDestinationIdAndUserId(destinationId, userId)
                .orElseThrow(() -> new RuntimeException("Review not found"));


        if (reviewDto.getReviewDesc() != null) {
            review.setReviewDesc(reviewDto.getReviewDesc());
        }

        if (reviewDto.getReviewRating() != null) {
            review.setReviewRating(reviewDto.getReviewRating());
        }


        return reviewRepository.save(review);
    }

    @Override
    public int findReviewRatingByDestinationIdAndUserId(int destinationId, int userId) {
        Review review=reviewRepository.findReviewByDestinationIdAndUserId(destinationId,userId).get();


        return review.getReviewRating();
    }

    @Override
    public Double averageRatingFromDestinationId(int destinationId) {

        if(destinationsRepository.findByDestinationId(destinationId).isPresent()){

            return reviewRepository.averageRatingFromDestinationId(destinationId);
        }

        return 0.0;
    }
}
