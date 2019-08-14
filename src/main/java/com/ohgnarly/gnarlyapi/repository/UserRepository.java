package com.ohgnarly.gnarlyapi.repository;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user) throws GnarlyException;

    List<User> getUsers() throws GnarlyException;

    User getUser(String userId) throws GnarlyException;

    boolean doesEmailExist(String emailAddress) throws GnarlyException;

    boolean doesUserNameExist(String userName) throws GnarlyException;

    User validateUserPassword(String userName, String password) throws GnarlyException;

    User validateChatUserPassword(String userName, String password) throws GnarlyException;
}
