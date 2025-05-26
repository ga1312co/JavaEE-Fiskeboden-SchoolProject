package fiskeboden.ejb.ics;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int supplierId;

    private String supplierNo;

    private String supplierName;

    private String supplierEmail;

    private String supplierPhoneNumber;

    private String supplierLocation;

    private String supplierSwishNumber;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Batch> batches = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierPickupPoint> supplierPickupPoints = new ArrayList<>();

    // Getters and Setters

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(String supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public String getSupplierPhoneNumber() {
        return supplierPhoneNumber;
    }

    public void setSupplierPhoneNumber(String supplierPhoneNumber) {
        this.supplierPhoneNumber = supplierPhoneNumber;
    }

    public String getSupplierLocation() {
        return supplierLocation;
    }

    public void setSupplierLocation(String supplierLocation) {
        this.supplierLocation = supplierLocation;
    }

    public String getSupplierSwishNumber() {
        return supplierSwishNumber;
    }

    public void setSupplierSwishNumber(String supplierSwishNumber) {
        this.supplierSwishNumber = supplierSwishNumber;
    }

    public List<Batch> getBatches() {
        return batches;
    }

    public void setBatches(List<Batch> batches) {
        this.batches = batches;
    }

    public List<SupplierPickupPoint> getSupplierPickupPoints() {
        return supplierPickupPoints;
    }

    public void setSupplierPickupPoints(List<SupplierPickupPoint> supplierPickupPoints) {
        this.supplierPickupPoints = supplierPickupPoints;
    }
}
