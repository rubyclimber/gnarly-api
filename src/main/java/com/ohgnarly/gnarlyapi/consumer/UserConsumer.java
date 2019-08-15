package com.ohgnarly.gnarlyapi.consumer;

import com.ohgnarly.gnarlyapi.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class UserConsumer implements Consumer<User> {
    private List<User> users;

    public List<User> getUsers() {
        return this.users;
    }

    public UserConsumer() {
        this.users = new ArrayList<>();
    }

    @Override
    public void accept(User user) {
        this.users.add(user);
    }

    public void clear() {
        this.users.clear();
    }
}
