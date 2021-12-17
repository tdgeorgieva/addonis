package com.addonis.modelMappers;

import com.addonis.dtos.RatingDto;
import com.addonis.models.addon.Rating;
import com.addonis.repositories.addon.AddonRepository;
import com.addonis.repositories.addon.RatingRepository;
import com.addonis.repositories.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class RatingModelMapper {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final AddonRepository addonRepository;

    public RatingModelMapper(RatingRepository ratingRepository, UserRepository userRepository, AddonRepository addonRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.addonRepository = addonRepository;
    }

    public Rating fromDto(RatingDto ratingDto) {
        Rating rating = new Rating();
        dtoToObject(ratingDto, rating);
        return rating;
    }

    public Rating fromDto(RatingDto ratingDto, int id) {
        Rating rating = ratingRepository.getById(id);
        dtoToObject(ratingDto, rating);
        return rating;
    }

    private void dtoToObject(RatingDto ratingDto, Rating rating) {
        rating.setRating(ratingDto.getRating());
        rating.setUser(userRepository.getById(ratingDto.getUserId()));
        rating.setAddon(addonRepository.getById(ratingDto.getAddonId()));
    }
}

