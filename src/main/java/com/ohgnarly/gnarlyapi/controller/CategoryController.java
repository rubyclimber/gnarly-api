package com.ohgnarly.gnarlyapi.controller;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Category;
import com.ohgnarly.gnarlyapi.repository.CategoryRepository;
import com.ohgnarly.gnarlyapi.response.CategoriesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class CategoryController {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping(value = "/categories", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoriesResponse> getCategories() throws GnarlyException {
        List<Category> categories = categoryRepository.getCategories();
        CategoriesResponse categoriesResponse = new CategoriesResponse();
        categoriesResponse.setCategories(categories);
        return new ResponseEntity<>(categoriesResponse, OK);
    }
}
