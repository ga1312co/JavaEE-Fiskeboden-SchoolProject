package fiskeboden.facade.ics;

import jakarta.ejb.Stateless;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ejb.EJB;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fiskeboden.eao.ics.BatchEAOLocal;
import fiskeboden.eao.ics.OrderBatchEAOLocal;
import fiskeboden.eao.ics.OrderRequestEAOLocal;
import fiskeboden.ejb.ics.Batch;
import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderRequest;
import fiskeboden.ejb.ics.OrderStatus;
import fiskeboden.ejb.ics.Supplier;
import fiskeboden.ejb.interceptors.LogInterceptor;

@Stateless
@Interceptors(LogInterceptor.class) // for logging
public class OrderRequestFacade implements OrderRequestFacadeLocal {

    @EJB
    private OrderRequestEAOLocal orderRequestEAO;

    @EJB
    private BatchEAOLocal batchEAO;

    @EJB
    private OrderBatchEAOLocal orderBatchEAO;

    public OrderRequest createOrder(int batchId, int quantity, String customerName, OrderStatus orderStatus /* boolean isApproved*/) {
    	//create new order
        OrderRequest newOrder = new OrderRequest();
        newOrder.setOrderCustomerName(customerName);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setOrderNo("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
      //  newOrder.setApproved(false);
        newOrder.setOrderStatus(OrderStatus.PENDING);

        newOrder = orderRequestEAO.create(newOrder);
        
        //find batch
        Batch batch = batchEAO.findByBatchId(batchId);
        if (batch == null) {
            throw new EntityNotFoundException("Batch with ID " + batchId + " not found.");
        }
        
        //check if < 0
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        
        //check if > batch quantity
        BigDecimal requestedQuantity = BigDecimal.valueOf(quantity);
        if (requestedQuantity.compareTo(batch.getBatchQuantity()) > 0) { 
            throw new IllegalArgumentException("Requested quantity exceeds available stock.");
        }
        
        //create new order batch
        OrderBatch orderBatch = new OrderBatch(newOrder, batch, BigDecimal.valueOf(quantity));
        orderBatchEAO.create(orderBatch);

        return newOrder;
    }

    public List<OrderRequest> getAllOrderRequests() {
        return orderRequestEAO.findAll();
    }

    public OrderRequest findOrderById(int orderId) {
        return orderRequestEAO.findById(orderId);
    }

    public void updateOrder(OrderRequest orderRequest) {
        orderRequestEAO.update(orderRequest);
    }

    public void deleteOrder(int orderId) {
        orderRequestEAO.delete(orderId);
    }
    
    public void denyOrderRequest(int orderId) {
        OrderRequest order = orderRequestEAO.findById(orderId);
        if (order == null) {
            throw new EntityNotFoundException("Order med ID " + orderId + " hittades inte.");
        }

        for (OrderBatch orderBatch : order.getOrderBatches()) {
            if (orderBatch.getOrderStatus() != OrderStatus.DENIED) {
                orderBatch.setOrderStatus(OrderStatus.DENIED);
                orderBatchEAO.update(orderBatch);
            }
        }

        order.setOrderStatus(OrderStatus.DENIED);
        orderRequestEAO.update(order);
    }
		
					
    
    @Override
    public void approveOrderRequest(int orderId) {
        OrderRequest orderRequest = orderRequestEAO.findById(orderId);
        if (orderRequest == null) {
            throw new EntityNotFoundException("OrderRequest with ID " + orderId + " not found.");
        }

        for (OrderBatch orderBatch : orderRequest.getOrderBatches()) {
            OrderStatus status = orderBatch.getOrderStatus();

            // Hoppa över redan hanterade
            if (status == OrderStatus.DENIED || status == OrderStatus.APPROVED) {
                continue;
            }

            Batch batch = batchEAO.findByBatchId(orderBatch.getId().getBatchId());
            if (batch == null) {
                throw new EntityNotFoundException("Batch with ID " + orderBatch.getId().getBatchId() + " not found.");
            }

            BigDecimal orderedQty = orderBatch.getOrderQuantity();
            BigDecimal availableQty = batch.getBatchQuantity();

            if (orderedQty.compareTo(availableQty) > 0) {
                throw new IllegalStateException("Beställd kvantitet (" + orderedQty + ") överskrider tillgängligt lagersaldo (" + availableQty + ") för batch " + batch.getBatchNo());
            }

            // Minska lagret
            batch.setBatchQuantity(availableQty.subtract(orderedQty));
            batchEAO.updateBatch(batch);

            // Godkänn batch
            orderBatch.setOrderStatus(OrderStatus.APPROVED);
            orderBatchEAO.update(orderBatch);
        }

        // Kontrollera att alla batchar nu är hanterade (APPROVED eller DENIED)
        boolean allBatchesProcessed = orderRequest.getOrderBatches().stream()
            .allMatch(b -> b.getOrderStatus() == OrderStatus.APPROVED || b.getOrderStatus() == OrderStatus.DENIED);

        if (!allBatchesProcessed) {
            throw new IllegalStateException("Alla batchar kunde inte hanteras. Vissa saknar lager eller status.");
        }

        // Slutgiltigt godkänn ordern
        orderRequest.setOrderStatus(OrderStatus.APPROVED);
        orderRequestEAO.update(orderRequest);
    }
    @Override 

    public Map<Supplier, List<OrderBatch>> getOrderBatchesGroupedBySupplier(int orderId) { 

    return orderRequestEAO.getOrderBatchesGroupedBySupplier(orderId); 

    } 

    @Override 

    public OrderRequest getOrderRequestById(int orderId) { 

    return orderRequestEAO.findOrderRequestById(orderId); 

    } 
}