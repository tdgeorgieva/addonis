package com.addonis.services.review;

import com.addonis.models.Review;

import java.util.List;

public interface ReviewService {

    List<Review> getAll();

    Review getById(int id);

    List<Review> getByUser(int userId);

    void create(Review review);

    void delete(int id);

    List<Review> getByAddon(int id);
}
