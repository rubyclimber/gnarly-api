package com.ohgnarly.gnarlyapi.controller;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Message;
import com.ohgnarly.gnarlyapi.repository.MessageRepository;
import com.ohgnarly.gnarlyapi.request.MessageRequest;
import com.ohgnarly.gnarlyapi.request.MessagesRequest;
import com.ohgnarly.gnarlyapi.response.MessageResponse;
import com.ohgnarly.gnarlyapi.response.MessagesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class MessageController {
    private MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping(value = "/messages")
    public ResponseEntity<MessagesResponse> getMessages(@RequestParam(required = false) Integer pageNumber) throws GnarlyException {
        pageNumber = isNull(pageNumber) ? 0 : pageNumber;
        List<Message> messages = messageRepository.getMessages(pageNumber);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setMessages(messages);
        return new ResponseEntity<>(messagesResponse, OK);
    }

    @PostMapping(value = "/message", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageResponse> createMessage(@RequestBody MessageRequest messageRequest) throws GnarlyException {
        Message message = messageRepository.addMessage(messageRequest.getMessage());
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);
        return new ResponseEntity<>(messageResponse, OK);
    }

    @PostMapping(value = "/messages", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<MessagesResponse> getMessages(@RequestBody MessagesRequest messagesRequest) throws GnarlyException {
        List<Message> messages = messageRepository.searchMessages(messagesRequest.getSearchText(),
                messagesRequest.getSearchDateValue());
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setMessages(messages);
        return new ResponseEntity<>(messagesResponse, OK);
    }
}
