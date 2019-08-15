package com.ohgnarly.gnarlyapi.consumer;

import com.ohgnarly.gnarlyapi.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component
public class CategoryConsumer implements Consumer<Category> {
    private List<Category> categories;

    public List<Category> getCategories() {
        return this.categories;
    }

    public CategoryConsumer() {
        this.categories = new ArrayList<>();
    }

    @Override
    public void accept(Category category) {
        this.categories.add(category);
    }

    public void clear() {
        this.categories.clear();
    }
}
