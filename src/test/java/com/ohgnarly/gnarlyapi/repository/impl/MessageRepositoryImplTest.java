package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.ohgnarly.gnarlyapi.consumer.MessageConsumer;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Message;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageRepositoryImplTest {
    @InjectMocks
    public MessageRepositoryImpl messageRepository;

    @Mock
    public MongoCollection<Message> mockMessageCollection;

    @Mock
    private FindIterable<Message> mockFindIterable;

    @Mock
    private MessageConsumer mockMessageConsumer;

    @Test
    public void getMessages() throws Throwable {
        //arrange
        Message message = new Message();

        when(mockFindIterable.sort(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.skip(anyInt())).thenReturn(mockFindIterable);
        when(mockFindIterable.limit(anyInt())).thenReturn(mockFindIterable);
        when(mockMessageCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockMessageConsumer.getMessages()).thenReturn(singletonList(message));

        //act
        List<Message> messages = messageRepository.getMessages(0);

        //assert
        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(message, messages.get(0));
    }

    @Test(expected = GnarlyException.class)
    public void getMessages_GivenMongoException() throws Throwable {
        //arrange
        Message message = new Message();

        when(mockMessageCollection.find(any(Bson.class))).thenThrow(MongoException.class);

        //act
        messageRepository.getMessages(0);
    }

    @Test
    public void addMessage() throws Throwable {
        //arrange
        Message message = new Message();

        //act
        Message addedMessage = messageRepository.addMessage(message);

        //arrange
        verify(mockMessageCollection, atLeastOnce()).insertOne(message);
        assertNotNull(addedMessage);
        assertEquals(message, addedMessage);
        assertNotNull(addedMessage.getCreatedAt());
        assertNotNull(addedMessage.getMessageId());
    }

    @Test(expected = GnarlyException.class)
    public void addMessage_GivenMongoException() throws Throwable {
        //arrange
        Message message = new Message();

        doThrow(MongoException.class).when(mockMessageCollection).insertOne(message);

        //act
        messageRepository.addMessage(message);
    }

    @Test
    public void testGetMessages_GivenSearchDate() throws Throwable {
        //arrange
        Message message = new Message();

        when(mockMessageCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockMessageConsumer.getMessages()).thenReturn(singletonList(message));

        //act
        List<Message> messages = messageRepository.searchMessages(null, LocalDate.now().minusDays(4));

        //assert
        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(message, messages.get(0));
    }

    @Test
    public void testGetMessages_GivenSearchText() throws Throwable {
        //arrange
        Message message = new Message();

        when(mockMessageCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockMessageConsumer.getMessages()).thenReturn(singletonList(message));

        //act
        List<Message> messages = messageRepository.searchMessages("hello", null);

        //assert
        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals(message, messages.get(0));
    }

    @Test(expected = GnarlyException.class)
    public void testGetMessages_GivenMongoException() throws Throwable {
        //arrange
        Message message = new Message();


        when(mockMessageCollection.find(any(Bson.class))).thenThrow(MongoException.class);

        //act
        messageRepository.searchMessages("hello", null);
    }
}