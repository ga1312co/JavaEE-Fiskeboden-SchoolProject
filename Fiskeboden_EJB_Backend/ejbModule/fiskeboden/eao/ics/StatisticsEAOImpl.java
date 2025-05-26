package fiskeboden.eao.ics;

import java.math.BigDecimal;

import fiskeboden.ejb.ics.OrderStatus;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Stateless
public class StatisticsEAOImpl implements StatisticsEAOLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long getTotalOrders() {
        Long result = em.createQuery(
            "SELECT COUNT(o) FROM OrderRequest o WHERE o.orderStatus = :status", Long.class)
            .setParameter("status", OrderStatus.APPROVED)
            .getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    public Double getTotalKgSold() {
        BigDecimal result = em.createQuery(
            "SELECT SUM(ob.orderQuantity) FROM OrderBatch ob " +
            "JOIN ob.batch b JOIN ob.orderRequest o " +
            "WHERE b.product.isMeasuredInUnits = false AND o.orderStatus = :status",
            BigDecimal.class)
            .setParameter("status", OrderStatus.APPROVED)
            .getSingleResult();

        return result != null ? result.doubleValue() : 0.0;
    }


    @Override
    public Long getTotalPickupPoints() {
        Long result = em.createQuery("SELECT COUNT(p.pickupPointId) FROM PickupPoint p", Long.class)
                        .getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    public Long getTotalProducts() {
        Long result = em.createQuery("SELECT COUNT(p.productId) FROM Product p", Long.class)
                        .getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    public Long getTotalSuppliers() {
        Long result = em.createQuery("SELECT COUNT(s.supplierId) FROM Supplier s", Long.class)
                        .getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    public Long getTotalUnitsSold() {
        BigDecimal result = em.createQuery(
                "SELECT SUM(ob.orderQuantity) FROM OrderBatch ob " +
                "JOIN ob.batch b JOIN ob.orderRequest o " +
                "WHERE b.product.isMeasuredInUnits = true AND o.orderStatus = :status",
                BigDecimal.class)
            .setParameter("status", OrderStatus.APPROVED)
            .getSingleResult();

        return result != null ? result.longValue() : 0L;
    }


    @Override
    public String findMostSoldProduct() {
        try {
            return em.createQuery(
                    "SELECT p.productName FROM OrderBatch ob " +
                    "JOIN ob.batch b JOIN ob.orderRequest o " +
                    "JOIN b.product p " +
                    "WHERE o.orderStatus = :status " +
                    "GROUP BY p.productName " +
                    "ORDER BY SUM(ob.orderQuantity) DESC",
                    String.class)
                .setParameter("status", OrderStatus.APPROVED)
                .setMaxResults(1)
                .getSingleResult();
        } catch (NoResultException e) {
            return "Ingen försäljning ännu";
        }
    }

    @Override
    public Long getTotalCategories() {
        Long result = em.createQuery("SELECT COUNT(c.categoryId) FROM Category c", Long.class)
                        .getSingleResult();
        return result != null ? result : 0L;
    }
}
