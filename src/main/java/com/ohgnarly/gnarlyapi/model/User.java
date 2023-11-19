package com.ohgnarly.gnarlyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
public class User {
    @BsonProperty("_id")
    private ObjectId id;
    @BsonProperty
    private String userName;
    @BsonProperty
    private String password;
    @BsonProperty
    private String firstName;
    @BsonProperty
    private String lastName;
    @BsonProperty
    private String emailAddress;
    @BsonProperty
    private LocalDateTime createdAt;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty
    @BsonIgnore
    public String getUserId() {
        return id.toHexString();
    }
}
