package com.example.myittaroostockinventorymanger.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Stock {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stock_id")
    private long stockId;
    private String name;

    public Stock(String name) {
        this.name = name;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockId=" + stockId +
                ", name='" + name + '\'' +
                '}';
    }
}
