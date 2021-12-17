package com.addonis.models.addon;

import com.addonis.models.user.User;

import java.io.Serializable;
import java.util.Objects;


public class RatingId implements Serializable {

    private Addon addon;

    private User user;

    // default constructor
    public RatingId() {
    }

    public RatingId(Addon addon, User user) {
        this.addon = addon;
        this.user = user;
    }

    // equals() and hashCode()

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return Objects.equals(addon, ratingId.addon) && Objects.equals(user, ratingId.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addon, user);
    }
}
