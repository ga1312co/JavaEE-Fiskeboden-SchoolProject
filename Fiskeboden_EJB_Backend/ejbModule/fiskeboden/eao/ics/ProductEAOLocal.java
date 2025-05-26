package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.Product;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface ProductEAOLocal {
    Product findByProductId(int id);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(int id);
    List<Product> findAll();
}
