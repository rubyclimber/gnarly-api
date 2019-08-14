package com.ohgnarly.gnarlyapi.comparator;

import com.ohgnarly.gnarlyapi.model.Message;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {
    @Override
    public int compare(Message msg1, Message msg2) {
        return msg1.getCreatedAt().compareTo(msg2.getCreatedAt());
    }
}
