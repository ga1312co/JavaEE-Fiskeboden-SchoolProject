package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.Product;
import java.util.List;

public interface ProductFacadeLocal {
    List<Product> getAllProducts();
    Product getProductById(int productId);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(int productId);
}