package fiskeboden.facade.ics;

import fiskeboden.eao.ics.StatisticsEAOLocal;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class StatisticsFacade implements StatisticsFacadeLocal {

    @EJB
    private StatisticsEAOLocal statisticsEAO;

    @Override
    public Long getTotalOrders() {
        return statisticsEAO.getTotalOrders();
    }

    @Override
    public Double getTotalKgSold() {
        return statisticsEAO.getTotalKgSold();
    }

    @Override
    public Long getTotalUnitsSold() {
        return statisticsEAO.getTotalUnitsSold();
    }

    @Override
    public String getMostSoldProduct() {
        return statisticsEAO.findMostSoldProduct();
    }

    @Override
    public Long getTotalPickupPoints() {
        return statisticsEAO.getTotalPickupPoints();
    }

    @Override
    public Long getTotalProducts() {
        return statisticsEAO.getTotalProducts();
    }

    @Override
    public Long getTotalSuppliers() {
        return statisticsEAO.getTotalSuppliers();
    }

    @Override
    public Long getTotalCategories() {
        return statisticsEAO.getTotalCategories();
    }
}
