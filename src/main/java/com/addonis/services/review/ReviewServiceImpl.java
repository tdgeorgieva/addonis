package com.addonis.services.review;

import com.addonis.models.Review;
import com.addonis.repositories.review.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.getAll();
    }

    @Override
    public void create(Review review) {
        reviewRepository.create(review);
    }

    @Override
    public List<Review> getByAddon(int id) {
        return reviewRepository.getByAddon(id);
    }


    @Override
    public Review getById(int id) {
        return reviewRepository.getById(id);
    }

    @Override
    public List<Review> getByUser(int userId) {
        return reviewRepository.getByUser(userId);
    }

    @Override
    public void delete(int id) {
        reviewRepository.delete(id);
    }
}
