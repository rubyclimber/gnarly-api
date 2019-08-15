package com.ohgnarly.gnarlyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDateTime;

public class Message {
    @BsonId
    private ObjectId id;
    @BsonProperty("messageBody")
    private String body;
    @BsonProperty
    private String userId;
    @BsonProperty
    private Instant createdAt;

    @JsonIgnore
    public ObjectId getId() {
        return id;
    }

    @JsonIgnore
    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty
    @BsonIgnore
    public String getMessageId() {
        return id.toHexString();
    }
}
