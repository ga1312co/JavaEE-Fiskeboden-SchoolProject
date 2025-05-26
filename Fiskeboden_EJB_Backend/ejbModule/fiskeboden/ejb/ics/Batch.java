package fiskeboden.ejb.ics;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity

public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int batchId;
    private String batchNo;
    private Integer batchWeek;
    private BigDecimal batchQuantity;
    private LocalDate productionDate;
    private int batchShelfLife;
    private BigDecimal batchPrice;
    private String batchOrigin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplierId")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product product;


    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }


    public Integer getBatchWeek() {
        return batchWeek;
    }

    public void setBatchWeek(Integer batchWeek) {
        this.batchWeek = batchWeek;
    }

    public BigDecimal getBatchQuantity() {
        return batchQuantity;
    }

    public void setBatchQuantity(BigDecimal batchQuantity) {
        this.batchQuantity = batchQuantity;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public int getBatchShelfLife() {
        return batchShelfLife;
    }

    public void setBatchShelfLife(int batchShelfLife) {
        this.batchShelfLife = batchShelfLife;
    }

    public BigDecimal getBatchPrice() {
        return batchPrice;
    }

    public void setBatchPrice(BigDecimal batchPrice) {
        this.batchPrice = batchPrice;
    }

    public String getBatchOrigin() {
        return batchOrigin;
    }

    public void setBatchOrigin(String batchOrigin) {
        this.batchOrigin = batchOrigin;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    // JPA Callbacks for logging tests
    @PostUpdate
    public void postUpdate() {
        System.out.println("ENTITY LOG: Batch updated. ID: " + batchId + ", New Quantity: " + batchQuantity);
    }

    @PrePersist
    public void prePersist() {
        System.out.println("ENTITY LOG: Batch about to be persisted. BatchNo: " + batchNo);
    }
}
