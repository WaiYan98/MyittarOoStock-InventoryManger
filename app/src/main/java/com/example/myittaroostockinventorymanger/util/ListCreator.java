package com.example.myittaroostockinventorymanger.util;

import com.example.myittaroostockinventorymanger.data.entities.Batch;
import com.example.myittaroostockinventorymanger.data.entities.ItemWithBatch;
import com.example.myittaroostockinventorymanger.data.entities.ItemBatch;

import java.util.ArrayList;
import java.util.List;

public class ListCreator {

    public static List<ItemBatch> createStockBatchList(List<ItemWithBatch> itemWithBatchList) {

        List<ItemBatch> itemBatchList = new ArrayList<>();

        for (ItemWithBatch itemWithBatch : itemWithBatchList) {

            for (Batch batch : itemWithBatch.getBatchList()) {

                itemBatchList.add(new ItemBatch(itemWithBatch.getItem(), batch));
            }
        }

        return itemBatchList;
    }
}
