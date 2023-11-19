package com.ohgnarly.gnarlyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class Message extends MongoModel {
    @BsonProperty("messageBody")
    private String body;
    @BsonProperty
    private String userId;
    @BsonProperty
    private Instant createdAt;

    @JsonProperty
    @BsonIgnore
    public String getMessageId() {
        return id.toHexString();
    }
}
