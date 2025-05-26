// --- OrderRequest.java ---
package fiskeboden.ejb.ics;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class OrderRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private String orderNo;
    private String orderCustomerName;
    private LocalDate orderDate;
    // private boolean isApproved;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


	@OneToMany(mappedBy = "orderRequest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderBatch> orderBatches = new HashSet<>();

    public OrderRequest() {
    	this.orderStatus = OrderStatus.PENDING;
    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderCustomerName() {
        return orderCustomerName;
    }

    public void setOrderCustomerName(String orderCustomerName) {
        this.orderCustomerName = orderCustomerName;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Set<OrderBatch> getOrderBatches() {
        return orderBatches;
    }

    public void setOrderBatches(Set<OrderBatch> orderBatches) {
        this.orderBatches = orderBatches;
    }

/*	public boolean isApproved() {
*		return isApproved;
*	}
*
*	public void setApproved(boolean isApproved) {
*		this.isApproved = isApproved; test
*	}
*/
    public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

    // JPA Callbacks for logging tests
    @PostUpdate
    public void postUpdate() {
        System.out.println("ENTITY LOG: OrderRequest updated. ID: " + orderId + ", Status: " + orderStatus);
    }

    @PostRemove
    public void postRemove() {
        System.out.println("ENTITY LOG: OrderRequest removed. ID: " + orderId);
    }

    @PrePersist
    public void prePersist() {
        System.out.println("ENTITY LOG: OrderRequest about to be persisted. Customer: " + orderCustomerName);
    }

}
