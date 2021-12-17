package com.addonis.repositories.addon;

import com.addonis.models.addon.Rating;


public interface RatingRepository {

    Rating ratingExistsForAddonEntity(int id, int addonId);

    Rating getById(int id);

    void create(Rating rating);

    void update(Rating rating);

    double calculateRating(int id);
}
