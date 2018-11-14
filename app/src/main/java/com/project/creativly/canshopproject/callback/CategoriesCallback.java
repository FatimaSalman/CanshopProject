package com.project.creativly.canshopproject.callback;


import com.project.creativly.canshopproject.model.Category;

public interface CategoriesCallback extends ErrorHandlerInterface {
    void onCategories(Category user);
}
