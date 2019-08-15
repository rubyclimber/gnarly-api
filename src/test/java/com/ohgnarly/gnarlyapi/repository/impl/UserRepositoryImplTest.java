package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.ohgnarly.gnarlyapi.consumer.UserConsumer;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.User;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryImplTest {
    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Mock
    private MongoCollection<User> mockUserCollection;

    @Mock
    private FindIterable<User> mockFindIterable;

    @Mock
    private UserConsumer mockUserConsumer;

    @Mock
    private BCryptPasswordEncoder mockBCryptPasswordEncoder;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void addUser() throws Throwable {
        User user = new User();
        user.setUserName("user name");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmailAddress("test@test.com");
        user.setPassword("password");

        User addedUser = userRepository.addUser(user);

        verify(mockUserCollection, atLeastOnce()).insertOne(user);
        assertNotNull(addedUser.getId());
        assertNotNull(addedUser.getCreatedAt());
        assertEquals(user.getFirstName(), addedUser.getFirstName());
        assertEquals(user.getLastName(), addedUser.getLastName());
        assertEquals(user.getEmailAddress(), addedUser.getEmailAddress());
        assertEquals(user.getUserName(), addedUser.getUserName());
        assertEquals(user.getUserName(), addedUser.getUserName());
    }

    @Test(expected = GnarlyException.class)
    public void addUser_GivenMongoException() throws Throwable {
        User user = new User();
        user.setUserName("user name");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmailAddress("test@test.com");
        user.setPassword("password");

        doThrow(MongoException.class).when(mockUserCollection).insertOne(user);

        userRepository.addUser(user);
    }

    @Test
    public void getUsers() throws Throwable {
        //arrange
        User user = new User();

        when(mockUserCollection.find()).thenReturn(mockFindIterable);
        when(mockUserConsumer.getUsers()).thenReturn(singletonList(user));

        //act
        List<User> users = userRepository.getUsers();

        //assert
        assertNotNull(users);
        assertTrue(users.size() > 0);
        assertEquals(user, users.get(0));
    }

    @Test(expected = GnarlyException.class)
    public void getUsers_GivenMongoException() throws Throwable {
        //arrange
        User user = new User();

        when(mockUserCollection.find()).thenThrow(MongoException.class);

        //act
        userRepository.getUsers();
    }

    @Test
    public void getUser() throws Throwable {
        //arrange
        String userId = "58cb3dd6692c796b68ff33ec";
        User user = new User();

        when(mockFindIterable.first()).thenReturn(user);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        //act
        User actualUser = userRepository.getUser(userId);

        //assert
        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

    @Test(expected = GnarlyException.class)
    public void getUser_GivenMongoException() throws Throwable {
        //arrange
        String userId = "58cb3dd6692c796b68ff33ec";
        User user = new User();

        when(mockUserCollection.find(any(Bson.class))).thenThrow(MongoException.class);

        //act
        userRepository.getUser(userId);
    }

    @Test
    public void doesEmailExist_GivenNewEmail() throws Throwable {
        //arrange
        String emailAddr = "test@test.com";

        when(mockFindIterable.first()).thenReturn(null);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        //act
        boolean result = userRepository.doesEmailExist(emailAddr);

        //assert
        assertFalse(result);
    }

    @Test(expected = GnarlyException.class)
    public void doesEmailExist_GivenMongoException() throws Throwable {
        //arrange
        String emailAddr = "test@test.com";

        when(mockUserCollection.find(any(Bson.class))).thenThrow(MongoException.class);

        //act
        userRepository.doesEmailExist(emailAddr);
    }

    @Test
    public void doesEmailExist_GivenExistingEmail() throws Throwable {
        //arrange
        String emailAddr = "test@test.com";

        when(mockFindIterable.first()).thenReturn(new User());
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        //act
        boolean result = userRepository.doesEmailExist(emailAddr);

        //assert
        assertTrue(result);
    }

    @Test
    public void doesUserNameExist_GivenNewUserName() throws Throwable {
        //arrange
        String userName = "aUserName";

        when(mockFindIterable.first()).thenReturn(null);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        //act
        boolean result = userRepository.doesUserNameExist(userName);

        //assert
        assertFalse(result);
    }

    @Test(expected = GnarlyException.class)
    public void doesUserNameExist_GivenMongoException() throws Throwable {
        //arrange
        String userName = "aUserName";

        when(mockUserCollection.find(any(Bson.class))).thenThrow(MongoException.class);

        //act
        userRepository.doesUserNameExist(userName);
    }

    @Test
    public void doesUserNameExist_GivenExistingUserName() throws Throwable {
        //arrange
        String userName = "aUserName";

        when(mockFindIterable.first()).thenReturn(new User());
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        //act
        boolean result = userRepository.doesUserNameExist(userName);

        //assert
        assertTrue(result);
    }

    @Test
    public void validateUserPassword_GivenValidUserNameAndPassword() throws Throwable {
        //arrange
        String userName = "validUserName";
        String password = "validPassword";
        User user = new User();
        user.setPassword("validPassword");

        when(mockFindIterable.first()).thenReturn(user);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockBCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);

        //act
        User actualUser = userRepository.validateUserPassword(userName, password);

        //assert
        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

    @Test(expected = GnarlyException.class)
    public void validateUserPassword_GivenInvalidUserName() throws Throwable {
        //arrange
        String userName = "invalidUserName";
        String password = "validPassword";

        when(mockFindIterable.first()).thenReturn(null);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        //act
        userRepository.validateUserPassword(userName, password);
    }

    @Test(expected = GnarlyException.class)
    public void validateUserPassword_GivenMongoException() throws Throwable {
        //arrange
        String userName = "invalidUserName";
        String password = "validPassword";

        when(mockUserCollection.find(any(Bson.class))).thenThrow(MongoException.class);

        //act
        userRepository.validateUserPassword(userName, password);
    }

    @Test(expected = GnarlyException.class)
    public void validateUserPassword_GivenInvalidPassword() throws Throwable {
        //arrange
        String userName = "validUserName";
        String password = "invalidPassword";
        User user = new User();
        user.setPassword("validPassword");

        when(mockFindIterable.first()).thenReturn(user);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockBCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);

        //act
        userRepository.validateUserPassword(userName, password);
    }

    @Test
    public void validateChatUserPassword_GivenValidUserNameAndPassword() throws Throwable {
        //arrange
        String userName = "validUserName";
        String password = "validPassword";
        User user = new User();
        user.setPassword("validPassword");

        when(mockFindIterable.first()).thenReturn(user);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockBCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(true);

        //act
        User actualUser = userRepository.validateChatUserPassword(userName, password);

        //assert
        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

    @Test(expected = GnarlyException.class)
    public void validateChatUserPassword_GivenInvalidUserName() throws Throwable {
        //arrange
        String userName = "invalidUserName";
        String password = "validPassword";

        when(mockFindIterable.first()).thenReturn(null);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        //act
        userRepository.validateChatUserPassword(userName, password);
    }

    @Test(expected = GnarlyException.class)
    public void validateChatUserPassword_GivenMongoException() throws Throwable {
        //arrange
        String userName = "invalidUserName";
        String password = "validPassword";

        when(mockUserCollection.find(any(Bson.class))).thenThrow(MongoException.class);

        //act
        userRepository.validateChatUserPassword(userName, password);
    }

    @Test(expected = GnarlyException.class)
    public void validateChatUserPassword_GivenInvalidPassword() throws Throwable {
        //arrange
        String userName = "validUserName";
        String password = "invalidPassword";
        User user = new User();
        user.setPassword("validPassword");

        when(mockFindIterable.first()).thenReturn(user);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockBCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);

        //act
        userRepository.validateChatUserPassword(userName, password);
    }
}