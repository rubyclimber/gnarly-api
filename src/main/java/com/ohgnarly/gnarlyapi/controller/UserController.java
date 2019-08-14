package com.ohgnarly.gnarlyapi.controller;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.User;
import com.ohgnarly.gnarlyapi.repository.UserRepository;
import com.ohgnarly.gnarlyapi.request.AvailabilityRequest;
import com.ohgnarly.gnarlyapi.request.LoginRequest;
import com.ohgnarly.gnarlyapi.request.UserRequest;
import com.ohgnarly.gnarlyapi.response.AvailabilityResponse;
import com.ohgnarly.gnarlyapi.response.LoginResponse;
import com.ohgnarly.gnarlyapi.response.UserResponse;
import com.ohgnarly.gnarlyapimodels.response.UsersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws GnarlyException {
        User user = userRepository.validateUserPassword(loginRequest.getUserName(), loginRequest.getPassword());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(true);
        loginResponse.setUser(user);
        return new ResponseEntity<>(loginResponse, OK);
    }

    @PostMapping(value = "/chat-login", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> chatLogin(@RequestBody LoginRequest loginRequest) throws GnarlyException {
        User user = userRepository.validateChatUserPassword(loginRequest.getUserName(), loginRequest.getPassword());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUser(user);
        loginResponse.setSuccess(true);
        return new ResponseEntity<>(loginResponse, OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<UsersResponse> getUsers() throws GnarlyException {
        List<User> users = userRepository.getUsers();
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setUsers(users);
        return new ResponseEntity<>(usersResponse, OK);
    }

    @GetMapping(value = "/user/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") String userId) throws GnarlyException {
        User user = userRepository.getUser(userId);
        UserResponse userResponse = new UserResponse();
        userResponse.setUser(user);
        return new ResponseEntity<>(userResponse, OK);
    }

    @PostMapping(value = "/user", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) throws GnarlyException {
        User user = userRepository.addUser(userRequest.getUser());
        UserResponse userResponse = new UserResponse();
        userResponse.setUser(user);
        return new ResponseEntity<>(userResponse, OK);
    }

    @PostMapping(value = "/check/username", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AvailabilityResponse> checkUserName(@RequestBody AvailabilityRequest availabilityRequest) throws GnarlyException {
        boolean doesUserNameExist = userRepository.doesUserNameExist(availabilityRequest.getUserName());
        AvailabilityResponse availabilityResponse = new AvailabilityResponse();
        availabilityResponse.setAvailable(!doesUserNameExist);
        return new ResponseEntity<>(availabilityResponse, OK);
    }

    @PostMapping(value = "/check/email", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AvailabilityResponse> checkEmailAddress(@RequestBody AvailabilityRequest availabilityRequest) throws GnarlyException {
        boolean doesEmailExist = userRepository.doesEmailExist(availabilityRequest.getEmailAddress());
        AvailabilityResponse availabilityResponse = new AvailabilityResponse();
        availabilityResponse.setAvailable(!doesEmailExist);
        return new ResponseEntity<>(availabilityResponse, OK);
    }
}
