package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
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

    @Autowired
    public CategoryRepositoryImpl(MongoCollection<Category> categoryCollection) {
        this.categoryCollection = categoryCollection;
    }

    @Override
    public List<Category> getCategories() throws GnarlyException {
        try {
            List<Category> categories = new ArrayList<>();
            FindIterable<Category> findIterable = categoryCollection.find();
            for (Category category : findIterable) {
                categories.add(category);
            }
            return categories;
        } catch (MongoException ex) {
            throw new GnarlyException("Error getting categories", ex);
        }
    }
}
