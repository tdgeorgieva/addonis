package com.addonis.services.rating;

import com.addonis.models.addon.Rating;


public interface RatingService {

    void create(Rating rating);

    void update(Rating rating);

    double calculateRating(int id);
}
