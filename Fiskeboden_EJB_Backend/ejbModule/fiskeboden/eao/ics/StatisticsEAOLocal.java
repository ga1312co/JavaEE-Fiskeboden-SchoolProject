package fiskeboden.eao.ics;

import jakarta.ejb.Local;

@Local
public interface StatisticsEAOLocal {

    Long getTotalOrders();

    Double getTotalKgSold();

    Long getTotalUnitsSold();

    String findMostSoldProduct();
    
    Long getTotalPickupPoints();
    
    Long getTotalProducts();
    
    Long getTotalSuppliers();
   
    Long getTotalCategories();
    
}