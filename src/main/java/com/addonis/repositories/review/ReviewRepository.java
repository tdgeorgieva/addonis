package com.addonis.repositories.review;

import com.addonis.models.Review;

import java.util.List;

public interface ReviewRepository {

    List<Review> getAll();

    Review getById(int id);

    List<Review> getByUser(int userId);

    void deleteByAddon(int addonId);

    void create(Review review);

    List<Review> getByAddon(int id);

    void delete(int id);

}
