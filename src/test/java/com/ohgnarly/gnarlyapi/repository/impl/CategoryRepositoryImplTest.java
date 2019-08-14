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
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

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
    private MongoCursor<Category> mockMongoCursor;

    @Test
    public void getCategories() throws Throwable {
        //arrange
        Category category = new Category();

        when(mockMongoCursor.hasNext()).thenReturn(true).thenReturn(false);
        when(mockMongoCursor.next()).thenReturn(category);
        when(mockFindIterable.iterator()).thenReturn(mockMongoCursor);
        when(mockCategoryCollection.find()).thenReturn(mockFindIterable);

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
        Category category = new Category();

        when(mockCategoryCollection.find()).thenThrow(MongoException.class);

        //act
        categoryRepository.getCategories();
    }
}