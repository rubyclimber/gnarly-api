package com.ohgnarly.gnarlyapi.consumer;

import com.ohgnarly.gnarlyapi.model.Message;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class MessageConsumer implements Consumer<Message> {
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public MessageConsumer() {
        messages = new ArrayList<>();
    }

    @Override
    public void accept(Message message) {
        messages.add(0, message);
    }

    public void clear() {
        this.messages.clear();
    }
}
