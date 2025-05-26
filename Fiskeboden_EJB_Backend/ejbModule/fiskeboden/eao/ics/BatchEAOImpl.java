package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.Batch;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class BatchEAOImpl implements BatchEAOLocal {

    @PersistenceContext(unitName = "FiskebodenPU")
    private EntityManager em;

    @Override
    public Batch findByBatchId(int id) {
        return em.find(Batch.class, id);
    }

    @Override
    public Batch createBatch(Batch batch) {
        em.persist(batch);
        return batch;
    }

    @Override
    public Batch updateBatch(Batch batch) {
        Batch updatedBatch = em.merge(batch);
        em.flush(); 
        return updatedBatch;
    }

    @Override
    public void deleteBatch(int id) {
        Batch b = findByBatchId(id);
        if (b != null) {
            em.remove(b);
        }
    }

    @Override
    public List<Batch> findAll() {
        return em.createQuery("SELECT b FROM Batch b", Batch.class).getResultList();
    }
    
    @Override
    public List<Batch> findByCategoryWeekAndPickup(String categoryName, int week, Integer pickupPointId) {
        String jpql = "SELECT b FROM Batch b " +
                      "JOIN b.product p " +
                      "JOIN b.supplier s " +
                      "JOIN s.supplierPickupPoints spp " +
                      "JOIN spp.pickupPoint pp " +
                      "WHERE p.category.categoryName = :categoryName " +
                      "AND b.batchWeek = :week";

        if (pickupPointId != null) {
            jpql += " AND pp.pickupPointId = :pickupPointId";
        }

        var query = em.createQuery(jpql, Batch.class)
                      .setParameter("categoryName", categoryName)
                      .setParameter("week", week);

        if (pickupPointId != null) {
            query.setParameter("pickupPointId", pickupPointId);
        }

        return query.getResultList();
    }
    
    @Override
    public List<Object[]> findBatchesWithPickupInfo(String categoryName, int week, Integer pickupPointId) {
        String jpql = "SELECT b, spp FROM Batch b " +
                      "JOIN b.supplier s " +
                      "JOIN s.supplierPickupPoints spp " +
                      "JOIN b.product p " +
                      "WHERE p.category.categoryName = :categoryName " +
                      "AND b.batchWeek = :week ";

        if (pickupPointId != null) {
            jpql += "AND spp.pickupPoint.pickupPointId = :pickupPointId ";
        }

        var query = em.createQuery(jpql, Object[].class)
                      .setParameter("categoryName", categoryName)
                      .setParameter("week", week);

        if (pickupPointId != null) {
            query.setParameter("pickupPointId", pickupPointId);
        }

        return query.getResultList(); // varje Object[] innehåller [Batch, SupplierPickupPoint]
    }

}