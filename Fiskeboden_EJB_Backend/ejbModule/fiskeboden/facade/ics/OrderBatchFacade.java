package fiskeboden.facade.ics;

import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderBatchId;
import fiskeboden.ejb.ics.OrderStatus;
import fiskeboden.ejb.interceptors.LogInterceptor;
import fiskeboden.eao.ics.OrderBatchEAOLocal;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.interceptor.Interceptors;

import java.math.BigDecimal;
import java.util.List;

@Stateless
@Interceptors(LogInterceptor.class) // for logging
public class OrderBatchFacade implements OrderBatchFacadeLocal {

    @EJB
    private OrderBatchEAOLocal orderBatchEAO;

    @Override
    public List<OrderBatch> getAllOrderBatches() {
        return orderBatchEAO.findAll();
    }

    @Override
    public OrderBatch getOrderBatchById(int orderId, int batchId) {
        return orderBatchEAO.findById(new OrderBatchId(orderId, batchId));
    }

    @Override
    public OrderBatch createOrderBatch(OrderBatch orderBatch) {
        if (orderBatch.getOrderStatus() == null) {
            orderBatch.setOrderStatus(OrderStatus.PENDING);
        }

        OrderBatchId id = orderBatch.getId();
        OrderBatch existing = orderBatchEAO.findById(id);

        if (existing != null) {
            BigDecimal combinedQty = existing.getOrderQuantity().add(orderBatch.getOrderQuantity());
            existing.setOrderQuantity(combinedQty);
            return orderBatchEAO.update(existing);
        }

        return orderBatchEAO.create(orderBatch);
    }




    @Override
    public void updateOrderBatch(OrderBatch orderBatch) {
        orderBatchEAO.update(orderBatch);
    }

    @Override
    public void deleteOrderBatch(int orderId, int batchId) {
        orderBatchEAO.delete(new OrderBatchId(orderId, batchId));
    }
    
    @Override
    public void denyOrderBatch(int orderId, int batchId) {
		OrderBatch orderBatch = orderBatchEAO.findById(new OrderBatchId(orderId, batchId));
		if (orderBatch != null) {
			orderBatch.setOrderStatus(OrderStatus.DENIED);
			orderBatchEAO.update(orderBatch);
		}
	}
    
    @Override
    public void approveBatch(int orderId, int batchId) {
        OrderBatch orderBatch = orderBatchEAO.findById(new OrderBatchId(orderId, batchId));
        if (orderBatch != null) {
            orderBatch.setOrderStatus(OrderStatus.APPROVED);
            orderBatchEAO.update(orderBatch);
        }
    }

}