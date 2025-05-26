package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.Product;
import fiskeboden.eao.ics.ProductEAOLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;

@Stateless
public class ProductFacade implements ProductFacadeLocal {

    @EJB
    private ProductEAOLocal productEAO;

    @Override
    public List<Product> getAllProducts() {
        return productEAO.findAll();
    }

    @Override
    public Product getProductById(int productId) {
        return productEAO.findByProductId(productId);
    }

    @Override
    public Product createProduct(Product product) {
        return productEAO.createProduct(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productEAO.updateProduct(product);
    }

    @Override
    public void deleteProduct(int productId) {
        productEAO.deleteProduct(productId);
    }
}