package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.Category;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface CategoryEAOLocal {
    Category findByCategoryId(int id);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    void deleteCategory(int id);
    List<Category> findAll();
}
