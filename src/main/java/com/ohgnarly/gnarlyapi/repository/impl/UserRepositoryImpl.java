package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.User;
import com.ohgnarly.gnarlyapi.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.String.format;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private MongoCollection<User> userCollection;
    private MongoCollection<User> chatUserCollection;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserRepositoryImpl(MongoCollection<User> userCollection, MongoCollection<User> chatUserCollection,
                              BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userCollection = userCollection;
        this.chatUserCollection = chatUserCollection;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

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
            FindIterable<User> userFindIterable = userCollection.find();
            for (User user : userFindIterable) {
                users.add(user);
            }
            return users;
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
