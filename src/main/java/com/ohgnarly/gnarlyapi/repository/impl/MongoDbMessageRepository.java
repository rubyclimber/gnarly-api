package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Message;
import com.ohgnarly.gnarlyapi.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.descending;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Repository
@RequiredArgsConstructor
public class MongoDbMessageRepository implements MessageRepository {
    private final MongoCollection<Message> messageCollection;

    @Override
    public List<Message> getMessages(int pageNumber) throws GnarlyException {
        try {
            List<Message> messages = new ArrayList<>();
            LocalDateTime oldestDate = LocalDateTime.now().minusDays(14);
            int pageSize = 25;
            messageCollection
                    .find(gte("createdAt", oldestDate))
                    .sort(descending("createdAt"))
                    .skip(pageNumber * pageSize)
                    .limit(pageSize)
                    .forEach((Consumer<Message>)messages::add);
            return messages;
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
            List<Message> messages = new ArrayList<>();

            if (isNotBlank(searchText)) {
                messageCollection
                        .find(regex("messageBody", searchText))
                        .forEach((Consumer<Message>) messages::add);

                return messages;
            }

            if (searchDate != null) {
                String fieldName = "createdAt";
                LocalDate tomorrow = searchDate.plusDays(1);
                messageCollection
                        .find(and(gte(fieldName, searchDate), lte(fieldName, tomorrow)))
                        .forEach((Consumer<? super Message>) messages::add);

                return messages;
            }

            throw new GnarlyException("Invalid search criteria specified.");
        } catch (MongoException ex) {
            throw new GnarlyException("Error searching messages.", ex);
        }
    }
}
