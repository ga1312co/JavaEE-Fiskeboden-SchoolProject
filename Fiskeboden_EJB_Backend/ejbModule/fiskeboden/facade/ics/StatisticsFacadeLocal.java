package fiskeboden.facade.ics;

import jakarta.ejb.Local;

@Local
public interface StatisticsFacadeLocal {
    Long getTotalOrders();
    Double getTotalKgSold();
    Long getTotalUnitsSold();
    String getMostSoldProduct();
    Long getTotalPickupPoints();
    Long getTotalProducts();
    Long getTotalSuppliers();
    Long getTotalCategories();
}
