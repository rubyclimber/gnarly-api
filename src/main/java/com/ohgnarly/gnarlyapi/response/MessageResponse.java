package com.ohgnarly.gnarlyapi.response;

import com.ohgnarly.gnarlyapi.model.Message;

public class MessageResponse {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
