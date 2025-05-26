package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderBatchId;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface OrderBatchEAOLocal {
    OrderBatch findById(OrderBatchId id);
    OrderBatch create(OrderBatch orderBatch);
    OrderBatch update(OrderBatch orderBatch);
    void delete(OrderBatchId id);
    List<OrderBatch> findAll();
}