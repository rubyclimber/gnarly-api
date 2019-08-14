package com.ohgnarly.gnarlyapi.request;

import com.ohgnarly.gnarlyapi.model.Message;

public class MessageRequest {
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
