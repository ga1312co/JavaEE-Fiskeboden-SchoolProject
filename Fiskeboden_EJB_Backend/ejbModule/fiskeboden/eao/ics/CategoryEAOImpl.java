package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.Category;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CategoryEAOImpl implements CategoryEAOLocal {

    @PersistenceContext(unitName = "FiskebodenPU")
    private EntityManager em;

    @Override
    public Category findByCategoryId(int id) {
        return em.find(Category.class, id);
    }

    @Override
    public Category createCategory(Category category) {
        em.persist(category);
        return category;
    }

    @Override
    public Category updateCategory(Category category) {
        return em.merge(category);
    }

    @Override
    public void deleteCategory(int id) {
        Category category = findByCategoryId(id);
        if (category != null) {
            em.remove(category);
        }
    }

    @Override
    public List<Category> findAll() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }
}
