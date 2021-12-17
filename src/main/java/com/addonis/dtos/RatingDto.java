package com.addonis.dtos;


import javax.validation.constraints.NotNull;


public class RatingDto {

    private int addonId;

    private int userId;

    @NotNull(message = "Rating cannot be blank!")
    private int rating;

    public RatingDto() {
    }

    public int getAddonId() {
        return addonId;
    }

    public void setAddonId(int addonId) {
        this.addonId = addonId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
