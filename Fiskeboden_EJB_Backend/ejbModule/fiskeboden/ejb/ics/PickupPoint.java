package fiskeboden.ejb.ics;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PickupPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pickupPointId;

    private String pickupPointNo;

    private String pickupPointAddress;

    private String pickupPointName;

    @OneToMany(mappedBy = "pickupPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierPickupPoint> supplierPickupPoints = new ArrayList<>();

    // Getters and Setters

    public int getPickupPointId() {
        return pickupPointId;
    }

    public void setPickupPointId(int pickupPointId) {
        this.pickupPointId = pickupPointId;
    }

    public String getPickupPointNo() {
        return pickupPointNo;
    }

    public void setPickupPointNo(String pickupPointNo) {
        this.pickupPointNo = pickupPointNo;
    }

    public String getPickupPointAddress() {
        return pickupPointAddress;
    }

    public void setPickupPointAddress(String pickupPointAddress) {
        this.pickupPointAddress = pickupPointAddress;
    }

    public String getPickupPointName() {
        return pickupPointName;
    }

    public void setPickupPointName(String pickupPointName) {
        this.pickupPointName = pickupPointName;
    }

    public List<SupplierPickupPoint> getSupplierPickupPoints() {
        return supplierPickupPoints;
    }

    public void setSupplierPickupPoints(List<SupplierPickupPoint> supplierPickupPoints) {
        this.supplierPickupPoints = supplierPickupPoints;
    }
}
