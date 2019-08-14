package com.ohgnarly.gnarlyapi.controller;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.repository.UserRepository;
import com.ohgnarly.gnarlyapi.model.User;
import com.ohgnarly.gnarlyapi.request.AvailabilityRequest;
import com.ohgnarly.gnarlyapi.request.LoginRequest;
import com.ohgnarly.gnarlyapi.request.UserRequest;
import com.ohgnarly.gnarlyapi.response.AvailabilityResponse;
import com.ohgnarly.gnarlyapi.response.LoginResponse;
import com.ohgnarly.gnarlyapi.response.UserResponse;
import com.ohgnarly.gnarlyapimodels.response.UsersResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository mockUserRepository;

    @Test
    public void testLogin() throws Throwable {
        //arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("user name");
        loginRequest.setPassword("password");

        User user = new User();

        when(mockUserRepository.validateUserPassword(anyString(), anyString())).thenReturn(user);

        //act
        ResponseEntity<LoginResponse> responseEntity = userController.login(loginRequest);

        //assert
        assertValidResponse(responseEntity);
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals(user, responseEntity.getBody().getUser());
    }

    @Test(expected = GnarlyException.class)
    public void testLogin_GivenInvalidUserLogin() throws Throwable {
        //arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("user name");
        loginRequest.setPassword("password");

        when(mockUserRepository.validateUserPassword(anyString(), anyString())).thenThrow(GnarlyException.class);

        //act
        ResponseEntity<LoginResponse> responseEntity = userController.login(loginRequest);
    }

    @Test
    public void testChatLogin() throws Throwable {
        //arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("user name");
        loginRequest.setPassword("password");
        User user = new User();

        when(mockUserRepository.validateChatUserPassword(anyString(), anyString())).thenReturn(user);

        //act
        ResponseEntity<LoginResponse> responseEntity = userController.chatLogin(loginRequest);

        //assert
        assertValidResponse(responseEntity);
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals(user, responseEntity.getBody().getUser());
    }

    @Test(expected = GnarlyException.class)
    public void testChatLogin_GivenInvalidChatUserLogin() throws Throwable {
        //arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("user name");
        loginRequest.setPassword("password");

        when(mockUserRepository.validateChatUserPassword(anyString(), anyString())).thenThrow(GnarlyException.class);

        //act
        ResponseEntity<LoginResponse> responseEntity = userController.chatLogin(loginRequest);
    }

    @Test
    public void testGetUsers() throws Throwable {
        //arrange
        List<User> users = singletonList(new User());

        when(mockUserRepository.getUsers()).thenReturn(users);

        //act
        ResponseEntity<UsersResponse> responseEntity = userController.getUsers();

        //assert
        assertValidResponse(responseEntity);
        assertEquals(users, responseEntity.getBody().getUsers());
    }

    @Test(expected = GnarlyException.class)
    public void testGetUsers_GivenThrowsGnarlyException() throws Throwable {
        //arrange
        List<User> users = singletonList(new User());

        when(mockUserRepository.getUsers()).thenThrow(GnarlyException.class);

        //act
        ResponseEntity<UsersResponse> responseEntity = userController.getUsers();
    }

    @Test
    public void testGetUser() throws Throwable {
        //arrange
        String userId = "user id";
        User user = new User();

        when(mockUserRepository.getUser(userId)).thenReturn(user);

        //act
        ResponseEntity<UserResponse> responseEntity = userController.getUser(userId);

        //assert
        assertValidResponse(responseEntity);
        assertEquals(user, responseEntity.getBody().getUser());
    }

    @Test(expected = GnarlyException.class)
    public void testGetUser_GivenInvalidUserId() throws Throwable {
        //arrange
        String userId = "user id";

        when(mockUserRepository.getUser(userId)).thenThrow(GnarlyException.class);

        //act
        ResponseEntity<UserResponse> responseEntity = userController.getUser(userId);
    }

    @Test
    public void testreateUser() throws Throwable {
        //arrange
        User user = new User();
        UserRequest userRequest = new UserRequest();
        userRequest.setUser(user);

        when(mockUserRepository.addUser(user)).thenReturn(user);

        //act
        ResponseEntity<UserResponse> responseEntity = userController.createUser(userRequest);

        //assert
        assertValidResponse(responseEntity);
        assertEquals(user, responseEntity.getBody().getUser());
    }

    @Test(expected = GnarlyException.class)
    public void testreateUser_GivenInvalidData() throws Throwable {
        //arrange
        User user = new User();
        UserRequest userRequest = new UserRequest();
        userRequest.setUser(user);

        when(mockUserRepository.addUser(user)).thenThrow(GnarlyException.class);

        //act
        ResponseEntity<UserResponse> responseEntity = userController.createUser(userRequest);
    }

    @Test
    public void testCheckUserName_GivenNewUserName() throws Throwable {
        //arrange
        AvailabilityRequest availabilityRequest = new AvailabilityRequest();
        availabilityRequest.setUserName("user name");

        when(mockUserRepository.doesUserNameExist(availabilityRequest.getUserName())).thenReturn(false);

        //act
        ResponseEntity<AvailabilityResponse> responseEntity = userController.checkUserName(availabilityRequest);

        //assert
        assertValidResponse(responseEntity);
        assertTrue(responseEntity.getBody().isAvailable());
    }

    @Test
    public void testCheckUserName_GivenExistingUserName() throws Throwable {
        //arrange
        AvailabilityRequest availabilityRequest = new AvailabilityRequest();
        availabilityRequest.setUserName("user name");

        when(mockUserRepository.doesUserNameExist(availabilityRequest.getUserName())).thenReturn(true);

        //act
        ResponseEntity<AvailabilityResponse> responseEntity = userController.checkUserName(availabilityRequest);

        //assert
        assertValidResponse(responseEntity);
        assertFalse(responseEntity.getBody().isAvailable());
    }

    @Test
    public void testCheckEmailAddress_GivenNewEmailAddress() throws Throwable {
        //arrange
        AvailabilityRequest availabilityRequest = new AvailabilityRequest();
        availabilityRequest.setEmailAddress("email address");

        when(mockUserRepository.doesEmailExist(availabilityRequest.getEmailAddress())).thenReturn(false);

        //act
        ResponseEntity<AvailabilityResponse> responseEntity = userController.checkEmailAddress(availabilityRequest);

        //assert
        assertValidResponse(responseEntity);
        assertTrue(responseEntity.getBody().isAvailable());
    }

    @Test
    public void testCheckEmailAddress_GivenExistingEmailAddress() throws Throwable {
        //arrange
        AvailabilityRequest availabilityRequest = new AvailabilityRequest();
        availabilityRequest.setEmailAddress("email address");

        when(mockUserRepository.doesEmailExist(availabilityRequest.getEmailAddress())).thenReturn(true);

        //act
        ResponseEntity<AvailabilityResponse> responseEntity = userController.checkEmailAddress(availabilityRequest);

        //assert
        assertValidResponse(responseEntity);
        assertFalse(responseEntity.getBody().isAvailable());
    }

    @Test(expected = GnarlyException.class)
    public void testCheckUserName_GivenNullUserName() throws Throwable {
        //arrange
        AvailabilityRequest availabilityRequest = new AvailabilityRequest();

        when(mockUserRepository.doesUserNameExist(null))
                .thenThrow(GnarlyException.class);

        //act
        ResponseEntity<AvailabilityResponse> responseEntity = userController.checkUserName(availabilityRequest);
    }

    @Test(expected = GnarlyException.class)
    public void testCheckEmailAddress_GivenNullEmailAddress() throws Throwable {
        //arrange
        AvailabilityRequest availabilityRequest = new AvailabilityRequest();

        when(mockUserRepository.doesEmailExist(null))
                .thenThrow(GnarlyException.class);

        //act
        ResponseEntity<AvailabilityResponse> responseEntity = userController.checkEmailAddress(availabilityRequest);
    }

    private void assertValidResponse(ResponseEntity responseEntity) {
        assertNotNull(responseEntity);
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}