package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderRequest;
import fiskeboden.ejb.ics.Supplier;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class OrderRequestEAOImpl implements OrderRequestEAOLocal {

    @PersistenceContext(unitName = "FiskebodenPU")
    private EntityManager em;

    @Override
    public OrderRequest findById(int id) {
        return em.find(OrderRequest.class, id);
    }

    @Override
    public OrderRequest create(OrderRequest orderRequest) {
        em.persist(orderRequest);
        return orderRequest;
    }

    @Override
    public OrderRequest update(OrderRequest orderRequest) {
        return em.merge(orderRequest);
    }

    @Override
    public void delete(int id) {
        OrderRequest or = findById(id);
        if (or != null) {
            em.remove(or);
        }
    }

    @Override
    public List<OrderRequest> findAll() {
        return em.createQuery("SELECT o FROM OrderRequest o", OrderRequest.class).getResultList();
    }
    
    @Override
    public Map<Supplier, List<OrderBatch>> getOrderBatchesGroupedBySupplier(int orderId) {
    	List<OrderBatch> batches = em.createQuery(
    		    "SELECT ob FROM OrderBatch ob " +
    		    "JOIN FETCH ob.batch " +
    		    "JOIN FETCH ob.batch.product " +
    		    "JOIN FETCH ob.batch.supplier " +
    		    "JOIN FETCH ob.batch.supplier.supplierPickupPoints " +
    		    "JOIN FETCH ob.batch.supplier.supplierPickupPoints.pickupPoint " +
    		    "WHERE ob.id.orderId = :orderId", OrderBatch.class)
    		    .setParameter("orderId", orderId)
    		    .getResultList();


        Map<Supplier, List<OrderBatch>> grouped = new HashMap<>();
        for (OrderBatch ob : batches) {
            Supplier supplier = ob.getBatch().getSupplier();
            grouped.computeIfAbsent(supplier, k -> new ArrayList<>()).add(ob);
        }
        return grouped;
    }

    public OrderRequest findOrderRequestById(int orderId) {
        return em.find(OrderRequest.class, orderId);
    }
} 
