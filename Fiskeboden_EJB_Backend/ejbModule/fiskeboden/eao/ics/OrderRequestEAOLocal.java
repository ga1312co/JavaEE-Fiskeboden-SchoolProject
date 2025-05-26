package fiskeboden.eao.ics;

import fiskeboden.ejb.ics.OrderBatch;
import fiskeboden.ejb.ics.OrderRequest;
import fiskeboden.ejb.ics.Supplier;
import jakarta.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface OrderRequestEAOLocal {
    OrderRequest findById(int id);
    OrderRequest create(OrderRequest orderRequest);
    OrderRequest update(OrderRequest orderRequest);
    void delete(int id);
    List<OrderRequest> findAll();
    Map<Supplier, List<OrderBatch>> getOrderBatchesGroupedBySupplier(int orderId);
    public OrderRequest findOrderRequestById(int orderId); 

}
