package com.ohgnarly.gnarlyapi.repository;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Message;

import java.time.LocalDate;
import java.util.List;

public interface MessageRepository {
    List<Message> getMessages(int pageNumber) throws GnarlyException;

    Message addMessage(Message message) throws GnarlyException;

    List<Message> searchMessages(String searchText, LocalDate searchDate) throws GnarlyException;
}
