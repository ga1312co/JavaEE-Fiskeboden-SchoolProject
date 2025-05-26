package fiskeboden.ejb.ics;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    private String productNo;
    private String productName;
    private boolean isMeasuredInUnits;

    @ManyToOne
    @JoinColumn(name = "categoryId") // Foreign key-kolumn i tabellen "Product"
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Batch> batches = new ArrayList<>();

    // --- Getters and Setters ---

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isMeasuredInUnits() {
        return isMeasuredInUnits;
    }

    public void setMeasuredInUnits(boolean isMeasuredInUnits) {
        this.isMeasuredInUnits = isMeasuredInUnits;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Batch> getBatches() {
        return batches;
    }

    public void setBatches(List<Batch> batches) {
        this.batches = batches;
    }
}
