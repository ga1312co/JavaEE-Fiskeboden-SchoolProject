package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.Category;
import fiskeboden.eao.ics.CategoryEAOLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class CategoryFacade implements CategoryFacadeLocal {

    @EJB
    private CategoryEAOLocal categoryEAO;

    @Override
    public List<Category> getAllCategories() {
        return categoryEAO.findAll();
    }

    @Override
    public Category getCategoryById(int id) {
        return categoryEAO.findByCategoryId(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryEAO.createCategory(category);
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryEAO.updateCategory(category);
    }

    @Override
    public void deleteCategory(int id) {
        categoryEAO.deleteCategory(id);
    }
}