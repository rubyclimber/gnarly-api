package com.ohgnarly.gnarlyapi.controller;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.repository.MessageRepository;
import com.ohgnarly.gnarlyapi.model.Message;
import com.ohgnarly.gnarlyapi.request.MessageRequest;
import com.ohgnarly.gnarlyapi.request.MessagesRequest;
import com.ohgnarly.gnarlyapi.response.MessageResponse;
import com.ohgnarly.gnarlyapi.response.MessagesResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {
    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageRepository messageRepository;

    @Test
    public void getMessages() throws Throwable {
        //arrange
        Message message = new Message();

        when(messageRepository.getMessages(anyInt())).thenReturn(singletonList(message));

        //act
        ResponseEntity<MessagesResponse> responseEntity = messageController.getMessages(1);

        //assert
        assertValidResponse(responseEntity);
        assertNotNull(responseEntity.getBody().getMessages());
        assertEquals(1, responseEntity.getBody().getMessages().size());
        assertEquals(message, responseEntity.getBody().getMessages().get(0));
    }

    @Test(expected = GnarlyException.class)
    public void getMessages_GivenGnarlyException() throws Throwable {
        //arrange
        Message message = new Message();

        when(messageRepository.getMessages(anyInt())).thenThrow(GnarlyException.class);

        //act
        messageController.getMessages(1);
    }

    @Test
    public void createMessage() throws Throwable {
        //arrange
        Message message = new Message();
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage(message);

       when(messageRepository.addMessage(message)).thenReturn(message);

        //act
        ResponseEntity<MessageResponse> responseEntity = messageController.createMessage(messageRequest);

        //assert
        assertValidResponse(responseEntity);
        assertNotNull(responseEntity.getBody().getMessage());
        assertEquals(message, responseEntity.getBody().getMessage());
    }

    @Test(expected = GnarlyException.class)
    public void createMessage_GivenGnarlyException() throws Throwable {
        //arrange
        Message message = new Message();
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setMessage(message);

        when(messageRepository.addMessage(message)).thenThrow(GnarlyException.class);

        //act
        messageController.createMessage(messageRequest);
    }

    @Test
    public void testGetMessages() throws Throwable {
        //arrange
        Message message = new Message();
        MessagesRequest messagesRequest = new MessagesRequest();

        when(messageRepository.searchMessages(messagesRequest.getSearchText(), messagesRequest.getSearchDateValue()))
                .thenReturn(singletonList(message));

        //act
        ResponseEntity<MessagesResponse> responseEntity = messageController.getMessages(messagesRequest);

        //assert
        assertValidResponse(responseEntity);
        assertNotNull(responseEntity.getBody().getMessages());
        assertEquals(1, responseEntity.getBody().getMessages().size());
        assertEquals(message, responseEntity.getBody().getMessages().get(0));
    }

    @Test(expected = GnarlyException.class)
    public void testGetMessages_GivenGnarlyException() throws Throwable {
        //arrange
        Message message = new Message();
        MessagesRequest messagesRequest = new MessagesRequest();

        when(messageRepository.searchMessages(messagesRequest.getSearchText(), messagesRequest.getSearchDateValue()))
                .thenThrow(GnarlyException.class);

        //act
        messageController.getMessages(messagesRequest);
    }

    private void assertValidResponse(ResponseEntity responseEntity) {
        assertNotNull(responseEntity);
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}