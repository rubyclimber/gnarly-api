package com.ohgnarly.gnarlyapi.response;

import com.ohgnarly.gnarlyapi.model.Category;

import java.util.List;

public class CategoriesResponse {
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
