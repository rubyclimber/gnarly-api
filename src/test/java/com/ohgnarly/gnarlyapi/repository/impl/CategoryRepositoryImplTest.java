package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.ohgnarly.gnarlyapi.consumer.CategoryConsumer;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryRepositoryImplTest {
    @InjectMocks
    private CategoryRepositoryImpl categoryRepository;

    @Mock
    private MongoCollection<Category> mockCategoryCollection;

    @Mock
    private FindIterable<Category> mockFindIterable;

    @Mock
    private CategoryConsumer mockCategoryConsumer;

    @Test
    public void getCategories() throws Throwable {
        //arrange
        Category category = new Category();

        when(mockCategoryCollection.find()).thenReturn(mockFindIterable);
        when(mockCategoryConsumer.getCategories()).thenReturn(singletonList(category));

        //act
        List<Category> categories = categoryRepository.getCategories();

        //assert
        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(category, categories.get(0));
    }

    @Test(expected = GnarlyException.class)
    public void getCategories_GivenMongoException() throws Throwable {
        //arrange
        when(mockCategoryCollection.find()).thenThrow(MongoException.class);

        //act
        categoryRepository.getCategories();
    }
}