package fiskeboden.ejb.ics;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderBatch {

    @EmbeddedId
    private OrderBatchId id;

    private BigDecimal orderQuantity;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    private OrderRequest orderRequest;

    @ManyToOne
    @JoinColumn(name = "batchId", insertable = false, updatable = false)
    private Batch batch;

    public OrderBatch() {
    	this.orderStatus = OrderStatus.PENDING;
    }

    public OrderBatch(OrderRequest orderRequest, Batch batch, BigDecimal orderQuantity) {
        this.orderRequest = orderRequest;
        this.batch = batch;
        this.orderQuantity = orderQuantity;
        this.id = new OrderBatchId(orderRequest.getOrderId(), batch.getBatchId());
        this.orderStatus = OrderStatus.PENDING;
    }

    // Getters and setters
    public OrderBatchId getId() {
        return id;
    }

    public void setId(OrderBatchId id) {
        this.id = id;
    }

    public BigDecimal getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(BigDecimal orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public OrderRequest getOrderRequest() {
        return orderRequest;
    }

    public void setOrderRequest(OrderRequest orderRequest) {
        this.orderRequest = orderRequest;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
    
	
    
}
