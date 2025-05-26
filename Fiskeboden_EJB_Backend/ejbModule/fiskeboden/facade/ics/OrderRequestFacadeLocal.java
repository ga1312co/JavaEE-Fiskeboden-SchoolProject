package fiskeboden.facade.ics;


import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderRequest;
import fiskeboden.ejb.ics.OrderStatus;
import fiskeboden.ejb.ics.Supplier;
import jakarta.ejb.Local;
import java.math.BigDecimal;
import java.time.LocalDate; 
import java.util.UUID; 
import jakarta.persistence.EntityNotFoundException; 
import java.util.List;
import java.util.Map;


@Local
public interface OrderRequestFacadeLocal {
    OrderRequest createOrder(int batchId, int quantity, String customerName, OrderStatus orderstatus/* boolean isApproved */) throws EntityNotFoundException, IllegalArgumentException;
    OrderRequest findOrderById(int orderId);
    List<OrderRequest> getAllOrderRequests();
    void updateOrder(OrderRequest orderRequest);
    void deleteOrder(int orderId);
    void approveOrderRequest(int orderId);
    void denyOrderRequest(int orderId);
    Map<Supplier, List<OrderBatch>> getOrderBatchesGroupedBySupplier(int orderId);
    OrderRequest getOrderRequestById(int orderId);


}