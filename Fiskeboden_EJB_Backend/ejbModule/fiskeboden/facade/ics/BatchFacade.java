package fiskeboden.facade.ics;

import jakarta.ejb.Stateless;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ejb.EJB;
import fiskeboden.eao.ics.BatchEAOLocal;
import fiskeboden.ejb.ics.Batch;
import fiskeboden.ejb.interceptors.LogInterceptor;

import java.util.List;

@Stateless
@Interceptors(LogInterceptor.class) // for logging
public class BatchFacade implements BatchFacadeLocal {
	
    @PersistenceContext(unitName = "FiskebodenPU")
    private EntityManager em;

    @EJB
    private BatchEAOLocal batchEAO;
    //get all batches available in the system
    public List<Batch> getAllBatches() {
        return batchEAO.findAll();
    }

    //get batch by id
    public Batch getBatchById(int id) {
        return batchEAO.findByBatchId(id);
    }

    //create a new batch
    public Batch createBatch(Batch batch) {
        return batchEAO.createBatch(batch);
    }

    //update an existing batch
    public Batch updateBatch(Batch batch) {
        return batchEAO.updateBatch(batch);
    }

    //delete a batch by id
    public void deleteBatch(int id) {
        batchEAO.deleteBatch(id);
    }
    
    @Override
    public List<Batch> findBatchesByCategoryWeekAndPickup(String category, int week, Integer pickupPointId) {
        return batchEAO.findByCategoryWeekAndPickup(category, week, pickupPointId);
    }

    public List<Object[]> getBatchesWithPickupInfo(String category, int week, Integer pickupPointId) {
        return batchEAO.findBatchesWithPickupInfo(category, week, pickupPointId);
    }



}
