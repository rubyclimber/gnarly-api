package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Category;
import com.ohgnarly.gnarlyapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Repository
@RequiredArgsConstructor
public class MongoDbCategoryRepository implements CategoryRepository {
    private final MongoCollection<Category> categoryCollection;

    @Override
    public List<Category> getCategories() throws GnarlyException {
        try {
            List<Category> categories = new ArrayList<>();
            categoryCollection.find().forEach((Consumer<? super Category>) categories::add);
            return categories;
        } catch (MongoException ex) {
            throw new GnarlyException("Error getting categories", ex);
        }
    }
}
