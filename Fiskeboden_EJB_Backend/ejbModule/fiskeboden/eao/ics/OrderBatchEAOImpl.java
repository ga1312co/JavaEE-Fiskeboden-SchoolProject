package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderBatchId;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class OrderBatchEAOImpl implements OrderBatchEAOLocal {

    @PersistenceContext(unitName = "FiskebodenPU")
    private EntityManager em;

    @Override
    public OrderBatch findById(OrderBatchId id) {
        return em.find(OrderBatch.class, id);
    }

    @Override
    public OrderBatch create(OrderBatch orderBatch) {
        em.persist(orderBatch);
        return orderBatch;
    }

    @Override
    public OrderBatch update(OrderBatch orderBatch) {
        return em.merge(orderBatch);
    }

    @Override
    public void delete(OrderBatchId id) {
        OrderBatch ob = findById(id);
        if (ob != null) {
            em.remove(ob);
        }
    }

    @Override
    public List<OrderBatch> findAll() {
        return em.createQuery("SELECT o FROM OrderBatch o", OrderBatch.class).getResultList();
    }
}
