package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.Category;
import java.util.List;

public interface CategoryFacadeLocal {
    List<Category> getAllCategories();
    Category getCategoryById(int id);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(int id);
}