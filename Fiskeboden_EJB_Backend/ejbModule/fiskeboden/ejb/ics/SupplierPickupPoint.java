package fiskeboden.ejb.ics;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class SupplierPickupPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int supplierPickupPointId;

    private int supplierId;

    private int pickupPointId;

    private byte pickupDay;

    private LocalTime pickupTime;

    @ManyToOne
    @JoinColumn(name = "pickupPointId", insertable = false, updatable = false)
    private PickupPoint pickupPoint;

    @ManyToOne
    @JoinColumn(name = "supplierId", insertable = false, updatable = false)
    private Supplier supplier;

    // Getters and Setters

    public int getSupplierPickupPointId() {
        return supplierPickupPointId;
    }

    public void setSupplierPickupPointId(int supplierPickupPointId) {
        this.supplierPickupPointId = supplierPickupPointId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getPickupPointId() {
        return pickupPointId;
    }

    public void setPickupPointId(int pickupPointId) {
        this.pickupPointId = pickupPointId;
    }

    public byte getPickupDay() {
        return pickupDay;
    }

    public void setPickupDay(byte pickupDay) {
        this.pickupDay = pickupDay;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public PickupPoint getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(PickupPoint pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
