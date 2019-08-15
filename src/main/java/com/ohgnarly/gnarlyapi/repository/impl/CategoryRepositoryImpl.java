package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.ohgnarly.gnarlyapi.consumer.CategoryConsumer;
import com.ohgnarly.gnarlyapi.controller.CategoryController;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Category;
import com.ohgnarly.gnarlyapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    private MongoCollection<Category> categoryCollection;
    private CategoryConsumer categoryConsumer;

    @Autowired
    public CategoryRepositoryImpl(MongoCollection<Category> categoryCollection, CategoryConsumer categoryConsumer) {
        this.categoryCollection = categoryCollection;
        this.categoryConsumer = categoryConsumer;
    }

    @Override
    public List<Category> getCategories() throws GnarlyException {
        try {
            categoryConsumer.clear();
            categoryCollection.find().forEach(categoryConsumer);
            return categoryConsumer.getCategories();
        } catch (MongoException ex) {
            throw new GnarlyException("Error getting categories", ex);
        }
    }
}
