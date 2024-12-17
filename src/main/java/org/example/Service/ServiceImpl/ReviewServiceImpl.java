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

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private DestinationsRepository destinationsRepository;

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

        review.setDestinationId(destination);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllEReviews() {
        return reviewRepository.findAll();
    }
}
