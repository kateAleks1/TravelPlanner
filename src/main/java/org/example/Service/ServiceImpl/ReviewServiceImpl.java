package org.example.Service.ServiceImpl;

import org.example.DTO.ReviewDto;
import org.example.Dal.Repository.ReviewRepository;
import org.example.Service.ReviewService;
import org.example.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
@Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review createReview(ReviewDto ReviewDto) {
        return null;
    }
}
