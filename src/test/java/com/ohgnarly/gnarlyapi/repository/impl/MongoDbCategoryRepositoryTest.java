package com.ohgnarly.gnarlyapi.repository.impl;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MongoDbCategoryRepositoryTest {
    @InjectMocks
    private MongoDbCategoryRepository categoryRepository;
    @Mock
    private MongoCollection<Category> mockCategoryCollection;
    @Spy
    private FindIterable<Category> mockFindIterable;
    @Mock
    private MongoCursor<Category> mockCursor;

    @Test
    public void getCategories() throws Throwable {
        //arrange
        Category category = new Category();

        when(mockCursor.hasNext()).thenReturn(true).thenReturn(false);
        when(mockCursor.next()).thenReturn(category);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCategoryCollection.find()).thenReturn(mockFindIterable);

        //act
        List<Category> results = categoryRepository.getCategories();

        //assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(category, results.get(0));
    }

    @Test(expected = GnarlyException.class)
    public void getCategories_GivenMongoException() throws Throwable {
        //arrange
        when(mockCategoryCollection.find()).thenThrow(MongoException.class);

        //act
        categoryRepository.getCategories();
    }
}