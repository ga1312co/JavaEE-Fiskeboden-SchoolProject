package fiskeboden.ejb.ics;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderBatchId implements Serializable {

    private int orderId;
    private int batchId;

    public OrderBatchId() {}

    public OrderBatchId(int orderId, int batchId) {
        this.orderId = orderId;
        this.batchId = batchId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderBatchId that = (OrderBatchId) o;
        return orderId == that.orderId && batchId == that.batchId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, batchId);
    }
} 