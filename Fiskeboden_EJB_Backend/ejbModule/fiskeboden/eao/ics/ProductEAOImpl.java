package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.Product;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ProductEAOImpl implements ProductEAOLocal {

    @PersistenceContext(unitName = "FiskebodenPU")
    private EntityManager em;

    @Override
    public Product findByProductId(int id) {
        return em.find(Product.class, id);
    }

    @Override
    public Product createProduct(Product product) {
        em.persist(product);
        return product;
    }

    @Override
    public Product updateProduct(Product product) {
        return em.merge(product);
    }

    @Override
    public void deleteProduct(int id) {
        Product product = findByProductId(id);
        if (product != null) {
            em.remove(product);
        }
    }

    @Override
    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }
}
