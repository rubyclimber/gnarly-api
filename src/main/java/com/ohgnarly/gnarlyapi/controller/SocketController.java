package com.ohgnarly.gnarlyapi.controller;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Message;
import com.ohgnarly.gnarlyapi.repository.MessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.List;

@Controller
public class SocketController {
    private MessageRepository messageRepository;

    public SocketController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/server-message")
    @SendTo("/chat-message")
    public Message createMessage(Message message) throws GnarlyException {
        return messageRepository.addMessage(message);
    }
}
