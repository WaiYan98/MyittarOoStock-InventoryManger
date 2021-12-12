package com.example.myittaroostockinventorymanger.util;

import com.example.myittaroostockinventorymanger.local.Batch;
import com.example.myittaroostockinventorymanger.local.StockWithBatch;
import com.example.myittaroostockinventorymanger.pojo.StockBatch;

import java.util.ArrayList;
import java.util.List;

public class ListCreator {

    public static List<StockBatch> createStockBatchList(List<StockWithBatch> stockWithBatchList) {

        List<StockBatch> stockBatchList = new ArrayList<>();

        for (StockWithBatch stockWithBatch : stockWithBatchList) {

            for (Batch batch : stockWithBatch.getBatchList()) {

                stockBatchList.add(new StockBatch(stockWithBatch.getStock(), batch));
            }
        }

        return stockBatchList;
    }
}
