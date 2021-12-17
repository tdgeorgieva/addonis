package com.addonis.services.rating;

import com.addonis.models.addon.Rating;
import com.addonis.repositories.addon.RatingRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }


    @Override
    public void create(Rating rating) {
        Rating r = ratingRepository.ratingExistsForAddonEntity(rating.getUser().getId(), rating.getAddon().getId());
        if(r != null) {
            rating.setId(r.getId());
            ratingRepository.update(rating);
        } else {
            ratingRepository.create(rating);
        }
    }

    @Override
    public void update(Rating rating) {
        ratingRepository.update(rating);
    }

    @Override
    public double calculateRating(int id) {
        return ratingRepository.calculateRating(id);
    }

}
