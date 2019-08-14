package com.ohgnarly.gnarlyapi.repository;

import com.ohgnarly.gnarlyapi.exception.GnarlyException;
import com.ohgnarly.gnarlyapi.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> getCategories() throws GnarlyException;
}
