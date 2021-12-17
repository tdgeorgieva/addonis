package com.addonis.dtos;

import java.time.LocalDate;

public class ReviewDto {

    private Integer userId;

    private String description;

    private LocalDate date;

    private Integer addonId;

    public ReviewDto() {
    }

    public ReviewDto(Integer userId, String description, Integer addonId) {
        this.userId = userId;
        this.description = description;
        this.addonId = addonId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getAddonId() {
        return addonId;
    }

    public void setAddonId(Integer addonId) {
        this.addonId = addonId;
    }
}
