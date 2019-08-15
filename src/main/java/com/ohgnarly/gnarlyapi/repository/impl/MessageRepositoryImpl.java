package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import com.ohgnarly.gnarlyapi.comparator.MessageComparator;
import com.ohgnarly.gnarlyapi.consumer.MessageConsumer;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Message;
import com.ohgnarly.gnarlyapi.repository.MessageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static java.util.Collections.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private MongoCollection<Message> messageCollection;
    private MessageConsumer messageConsumer;

    @Autowired
    public MessageRepositoryImpl(MongoCollection<Message> messageCollection, MessageConsumer messageConsumer) {
        this.messageCollection = messageCollection;
        this.messageConsumer = messageConsumer;
    }

    @Override
    public List<Message> getMessages(int pageNumber) throws GnarlyException {
        try {
            messageConsumer.clear();
            LocalDateTime oldestDate = LocalDateTime.now().minusDays(14);
            int pageSize = 25;
            messageCollection
                    .find(gte("createdAt", oldestDate))
                    .sort(descending("createdAt"))
                    .skip(pageNumber * pageSize)
                    .limit(pageSize)
                    .forEach(messageConsumer);
            return messageConsumer.getMessages();
        } catch (MongoException ex) {
            throw new GnarlyException("Error getting default messages", ex);
        }
    }

    @Override
    public Message addMessage(Message message) throws GnarlyException {
        try {
            message.setCreatedAt(Instant.now());
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
            messageConsumer.clear();

            if (isNotBlank(searchText)) {
                messageCollection
                        .find(regex("messageBody", searchText))
                        .forEach(messageConsumer);

                return messageConsumer.getMessages();
            }

            if (searchDate != null) {
                String fieldName = "createdAt";
                LocalDate today = searchDate;
                LocalDate tomorrow = searchDate.plusDays(1);
                messageCollection
                        .find(and(gte(fieldName, today), lte(fieldName, tomorrow)))
                        .forEach(messageConsumer);

                return messageConsumer.getMessages();
            }

            throw new GnarlyException("Invalid search criteria specified.");
        } catch (MongoException ex) {
            throw new GnarlyException("Error searching messages.", ex);
        }
    }
}
