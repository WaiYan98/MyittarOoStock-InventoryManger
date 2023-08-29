package com.example.myittaroostockinventorymanger.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class StockWithBatch {

    @Embedded
    private Stock stock;
    @Relation(parentColumn = "stock_id",
            entityColumn = "stock_id")
    private List<Batch> batchList;

    public StockWithBatch(Stock stock, List<Batch> batchList) {
        this.stock = stock;
        this.batchList = batchList;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public List<Batch> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<Batch> batchList) {
        this.batchList = batchList;
    }

    @Override
    public String toString() {
        return "StockWithBatch{" +
                "stock=" + stock +
                ", batchList=" + batchList +
                '}';
    }
}
