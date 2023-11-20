package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.User;
import com.ohgnarly.gnarlyapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.String.format;

@Repository
@RequiredArgsConstructor
public class MongoDbUserRepository implements UserRepository {
    private final MongoCollection<User> userCollection;
    private final MongoCollection<User> chatUserCollection;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User addUser(User user) throws GnarlyException {
        try {
            user.setCreatedAt(LocalDateTime.now());
            user.setId(new ObjectId());
            userCollection.insertOne(user);
            return user;
        } catch (MongoException ex) {
            throw new GnarlyException("Exception creating new user", ex);
        }
    }

    @Override
    public List<User> getUsers() throws GnarlyException {
        try {
            List<User> users = new ArrayList<>();
            userCollection.find().forEach((Consumer<User>) users::add);
            return users;
        } catch (MongoException ex) {
            throw new GnarlyException(ex);
        }
    }

    @Override
    public List<User> getChatUsers() throws GnarlyException {
        try {
            List<User> chatUsers = new ArrayList<>();
            chatUserCollection.find().forEach((Consumer<User>) chatUsers::add);
            return chatUsers;
        } catch (MongoException ex) {
            throw new GnarlyException(ex);
        }
    }

    @Override
    public User getUser(String userId) throws GnarlyException {
        try {
            return userCollection.find(eq("_id", new ObjectId(userId))).first();
        } catch (MongoException ex) {
            throw new GnarlyException(format("Error getting user with id %s", userId), ex);
        }
    }

    @Override
    public boolean doesEmailExist(String emailAddress) throws GnarlyException {
        try {
            User user = userCollection.find(eq("emailAddress", emailAddress)).first();
            return user != null;
        } catch (MongoException ex) {
            throw new GnarlyException("Error validating email address", ex);
        }
    }

    @Override
    public boolean doesUserNameExist(String userName) throws GnarlyException {
        try {
            User user = userCollection.find(eq("userName", userName)).first();
            return user != null;
        } catch (MongoException ex) {
            throw new GnarlyException("Error validating user name", ex);
        }
    }

    @Override
    public User validateUserPassword(String userName, String password) throws GnarlyException {
        try {
            User user = userCollection.find(eq("userName", userName)).first();

            if (user != null) {
                if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                    return user;
                }
            }

            throw new GnarlyException("Invalid login information provided.");
        } catch (MongoException ex) {
            throw new GnarlyException("Error validating user for login", ex);
        }
    }

    @Override
    public User validateChatUserPassword(String userName, String password) throws GnarlyException {
        try {
            User user = chatUserCollection.find(eq("userName", userName)).first();

            if (user != null) {
                if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                    return user;
                }
            }

            throw new GnarlyException("Invalid login information provided.");
        } catch (MongoException ex) {
            throw new GnarlyException("Error validating user for chat login", ex);
        }
    }
}
