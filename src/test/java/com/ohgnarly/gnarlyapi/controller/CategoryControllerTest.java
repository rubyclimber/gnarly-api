package com.ohgnarly.gnarlyapi.controller;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.repository.CategoryRepository;
import com.ohgnarly.gnarlyapi.model.Category;
import com.ohgnarly.gnarlyapi.response.CategoriesResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(MockitoJUnitRunner.class)
public class CategoryControllerTest {
    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryRepository mockCategoryRepository;

    @Test
    public void getCategories() throws Throwable {
        //arrange
        Category category = new Category();

        when(mockCategoryRepository.getCategories()).thenReturn(singletonList(category));

        //act
        ResponseEntity<CategoriesResponse> responseEntity = categoryController.getCategories();

        //assert
        assertNotNull(responseEntity);
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getCategories());
        assertEquals(1, responseEntity.getBody().getCategories().size());
        assertEquals(category, responseEntity.getBody().getCategories().get(0));
    }

    @Test(expected = GnarlyException.class)
    public void getCategories_GivenGnarlyException() throws Throwable {
        //arrange
        Category category = new Category();

        when(mockCategoryRepository.getCategories()).thenThrow(GnarlyException.class);

        //act
        categoryController.getCategories();
    }
}