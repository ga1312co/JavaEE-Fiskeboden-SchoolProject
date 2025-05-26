package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.OrderBatch;

import java.math.BigDecimal;
import java.util.List;

public interface OrderBatchFacadeLocal {
    List<OrderBatch> getAllOrderBatches();
    OrderBatch getOrderBatchById(int orderId, int batchId);
    OrderBatch createOrderBatch(OrderBatch orderBatch);
    void updateOrderBatch(OrderBatch orderBatch);
    void deleteOrderBatch(int orderId, int batchId);
    void denyOrderBatch(int orderId, int batchId);
    void approveBatch(int orderId, int batchId);
}