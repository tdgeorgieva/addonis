package com.addonis.models;

import com.addonis.models.user.User;
import com.addonis.models.addon.Addon;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "addon_id")
    private Addon addon;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private LocalDate date;

    public Review() {
    }

    public Review(int id, User user, Addon addon, String description, LocalDate date) {
        this.id = id;
        this.user = user;
        this.addon = addon;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Addon getAddon() {
        return addon;
    }

    public void setAddon(Addon addon) {
        this.addon = addon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
