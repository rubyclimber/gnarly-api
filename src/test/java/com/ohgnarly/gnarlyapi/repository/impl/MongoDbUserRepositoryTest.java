package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
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

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MongoDbUserRepositoryTest {
    private User user;
    @InjectMocks
    private MongoDbUserRepository userRepository;

    @Mock
    private MongoCollection<User> mockUserCollection;

    @Mock
    private FindIterable<User> mockFindIterable;

    @Mock
    private UserConsumer mockUserConsumer;

    @Mock
    private BCryptPasswordEncoder mockBCryptPasswordEncoder;

    @Before
    public void setUp() {
        user = new User();
        user.setUserName("user name");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmailAddress("test@test.com");
        user.setPassword("password");

        when(mockUserCollection.find()).thenReturn(mockFindIterable);
        when(mockUserCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(user);
    }

    @Test
    public void addUser() throws Throwable {
        User addedUser = userRepository.addUser(user);

        verify(mockUserCollection, atLeastOnce()).insertOne(user);
        assertEquals(user, addedUser);
    }

    @Test(expected = GnarlyException.class)
    public void addUser_GivenMongoException() throws Throwable {
        doThrow(MongoException.class).when(mockUserCollection).insertOne(user);

        userRepository.addUser(user);
    }

    @Test
    public void getUsers() throws Throwable {
        //arrange
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
        when(mockUserCollection.find()).thenThrow(MongoException.class);

        //act
        userRepository.getUsers();
    }

    @Test
    public void getUser() throws Throwable {
        //arrange
        String userId = "58cb3dd6692c796b68ff33ec";

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

        when(mockUserCollection.find(any(Bson.class))).thenThrow(MongoException.class);

        //act
        userRepository.getUser(userId);
    }

    @Test
    public void doesEmailExist_GivenNewEmail() throws Throwable {
        //arrange
        String emailAddr = "test@test.com";

        when(mockFindIterable.first()).thenReturn(null);

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
        user.setPassword(password);

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
        user.setPassword("validPassword");

        when(mockBCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);

        //act
        userRepository.validateUserPassword(userName, password);
    }

    @Test
    public void validateChatUserPassword_GivenValidUserNameAndPassword() throws Throwable {
        //arrange
        String userName = "validUserName";
        String password = "validPassword";
        user.setPassword(password);

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
        user.setPassword("validPassword");

        when(mockBCryptPasswordEncoder.matches(password, user.getPassword())).thenReturn(false);

        //act
        userRepository.validateChatUserPassword(userName, password);
    }
}