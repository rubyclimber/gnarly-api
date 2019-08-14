package com.ohgnarly.gnarlyapi.model;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

public class Category {
    @BsonProperty("categoryDesc")
    private String description;
    private LocalDateTime createdAt;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
