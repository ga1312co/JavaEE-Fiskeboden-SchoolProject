package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.Batch;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface BatchFacadeLocal {

    List<Batch> getAllBatches();
    Batch getBatchById(int id);
    Batch createBatch(Batch batch);
    Batch updateBatch(Batch batch);
    void deleteBatch(int id);
	List<Batch> findBatchesByCategoryWeekAndPickup(String category, int week, Integer pickupPointId);
	public List<Object[]> getBatchesWithPickupInfo(String category, int week, Integer pickupPointId);

}