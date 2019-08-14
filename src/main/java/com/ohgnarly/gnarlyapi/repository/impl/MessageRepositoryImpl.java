package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Message;
import com.ohgnarly.gnarlyapi.repository.MessageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private MongoCollection<Message> messageCollection;

    @Autowired
    public MessageRepositoryImpl(MongoCollection<Message> messageCollection) {
        this.messageCollection = messageCollection;
    }

    @Override
    public List<Message> getMessages() throws GnarlyException {
        try {
            List<Message> messages = new ArrayList<>();
            LocalDateTime oldestDate = LocalDateTime.now().minusDays(14);
            FindIterable<Message> findIterable = messageCollection.find(gte("createdAt", oldestDate));
            for (Message message : findIterable) {
                messages.add(message);
            }
            return messages;
        } catch (MongoException ex) {
            throw new GnarlyException("Error getting default messages", ex);
        }
    }

    @Override
    public Message addMessage(Message message) throws GnarlyException {
        try {
            message.setCreatedAt(LocalDateTime.now());
            message.setId(new ObjectId());
            messageCollection.insertOne(message);
            return message;
        } catch (MongoException ex) {
            throw new GnarlyException("Error creating new message.");
        }
    }

    @Override
    public List<Message> searchMessages(String searchText, LocalDate searchDate) throws GnarlyException {
        try {
            if (isNotBlank(searchText)) {
                FindIterable<Message> findIterable =
                        messageCollection.find(regex("messageBody", searchText));

                return loadMessagesFromIterable(findIterable);
            }

            if (searchDate != null) {
                String fieldName = "createdAt";
                LocalDate today = searchDate;
                LocalDate tomorrow = searchDate.plusDays(1);
                FindIterable<Message> findIterable =
                        messageCollection.find(and(gte(fieldName, today), lte(fieldName, tomorrow)));

                return loadMessagesFromIterable(findIterable);
            }

            throw new GnarlyException("Invalid search criteria specified.");
        } catch (MongoException ex) {
            throw new GnarlyException("Error searching messages.", ex);
        }
    }

    private List<Message> loadMessagesFromIterable(FindIterable<Message> findIterable) {
        List<Message> messages = new ArrayList<>();
        for (Message message : findIterable) {
            messages.add(message);
        }
        return messages;
    }
}
