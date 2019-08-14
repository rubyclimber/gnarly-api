package com.ohgnarly.gnarlyapi.response;

import com.ohgnarly.gnarlyapi.model.User;

public class LoginResponse {
    private boolean success;
    private User user;
    private String socketUrl = "http://localhost:1966/websocket";

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSocketUrl() {
        return socketUrl;
    }

    public void setSocketUrl(String socketUrl) {
        this.socketUrl = socketUrl;
    }
}
