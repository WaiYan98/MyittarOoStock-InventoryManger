package com.example.myittaroostockinventorymanger.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    private long transactionId;
    @ColumnInfo(name =  "batch_id")
    private long batchId;
    @ColumnInfo(name = "stock_in")
    private int stockIn;
    @ColumnInfo(name = "stock_out")
    private int stockOut;
    private double profit;
    private Date date;

    public Transaction(long batchId, int stockIn, int stockOut, double profit, Date date) {
        this.batchId = batchId;
        this.stockIn = stockIn;
        this.stockOut = stockOut;
        this.profit = profit;
        this.date = date;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public int getStockIn() {
        return stockIn;
    }

    public void setStockIn(int stockIn) {
        this.stockIn = stockIn;
    }

    public int getStockOut() {
        return stockOut;
    }

    public void setStockOut(int stockOut) {
        this.stockOut = stockOut;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", batchId=" + batchId +
                ", stockIn=" + stockIn +
                ", stockOut=" + stockOut +
                ", profit=" + profit +
                ", date=" + date +
                '}';
    }
}
