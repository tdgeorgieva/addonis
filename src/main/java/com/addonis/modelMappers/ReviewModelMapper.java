package com.addonis.modelMappers;

import com.addonis.dtos.ReviewDto;
import com.addonis.models.Review;
import com.addonis.repositories.addon.AddonRepository;
import com.addonis.repositories.review.ReviewRepository;
import com.addonis.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewModelMapper {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AddonRepository addonRepository;

    @Autowired
    public ReviewModelMapper(ReviewRepository reviewRepository, UserRepository userRepository, AddonRepository addonRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.addonRepository = addonRepository;
    }

    public Review fromDto(ReviewDto reviewDto) {
        Review review = new Review();
        dtoToObject(reviewDto, review);
        return review;
    }

    public Review fromDto(ReviewDto reviewDto, int id) {
        Review review = reviewRepository.getById(id);
        dtoToObject(reviewDto, review);
        return review;
    }

    private void dtoToObject(ReviewDto reviewDto, Review review) {
        review.setDate(reviewDto.getDate());
        review.setDescription(reviewDto.getDescription());
        review.setAddon(addonRepository.getById(reviewDto.getAddonId()));
        review.setUser(userRepository.getById(reviewDto.getUserId()));
    }
}
