package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.Batch;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface BatchEAOLocal {
    Batch findByBatchId(int id);
    Batch createBatch(Batch batch);
    Batch updateBatch(Batch batch);
    void deleteBatch(int id);
    List<Batch> findAll();
    List<Batch> findByCategoryWeekAndPickup(String categoryName, int week, Integer pickupPointId);
	List<Object[]> findBatchesWithPickupInfo(String categoryName, int week, Integer pickupPointId);

}
