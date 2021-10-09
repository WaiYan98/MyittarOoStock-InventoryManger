package com.example.myittaroostockinventorymanger.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Batch {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "batch_id")
    private long batchId;
    @ColumnInfo(name = "product_id")
    private long productId;
    @ColumnInfo(name = "original_price")
    private double originalPrice;
    @ColumnInfo(name = "sale_price")
    private double salePrice;
    @ColumnInfo(name = "total_stock")
    private int totalStock;
    @ColumnInfo(name = "exp_date")
    private Date expDate;

    public Batch(long productId, double originalPrice, double salePrice, int totalStock, Date expDate) {
        this.productId = productId;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.totalStock = totalStock;
        this.expDate = expDate;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchId=" + batchId +
                ", productId=" + productId +
                ", originalPrice=" + originalPrice +
                ", salePrice=" + salePrice +
                ", totalStock=" + totalStock +
                ", expDate=" + expDate +
                '}';
    }
}
