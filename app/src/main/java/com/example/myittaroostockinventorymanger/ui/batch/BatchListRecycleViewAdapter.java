package com.example.myittaroostockinventorymanger.ui.batch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.data.entities.StockBatch;
import com.example.myittaroostockinventorymanger.util.AutoNumGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BatchListRecycleViewAdapter extends RecyclerView.Adapter<BatchListRecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<StockBatch> stockBatchList;
    private boolean isSelectedMode = false;
    private List<Long> selectedBatchIdList = new ArrayList<>();
    private CallBack callBack;
    private ViewHolder holder;


    public BatchListRecycleViewAdapter(Context context, List<StockBatch> stockBatchList) {
        this.context = context;
        this.stockBatchList = stockBatchList;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_stock_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        this.holder = holder;
        StockBatch current = stockBatchList.get(position);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

        CardView cardView = holder.cardViewItemNameContainer;
        changeCardViewColor(cardView);

        holder.txtStockName.setText(current.getStock().getName());
        holder.txtNameInitialWord.setText(current.getStock().getName().substring(0, 1).toUpperCase());
        holder.txtCostPrice.setText(String.valueOf(current.getBatch().getOriginalPrice()));
        holder.txtSalePrice.setText(String.valueOf(current.getBatch().getSalePrice()));
        holder.txtExpDate.setText(df.format(current.getBatch().getExpDate()));
        holder.txtAmount.setText(String.valueOf(current.getBatch().getTotalStock()));

        //when batch item onLongClicked show contextual action mode
        holder.cardViewBatch.setOnLongClickListener(v -> {
            callBack.onLongClicked();
            isSelectedMode = true;
            return false;
        });

        //to insert selected Batch id to selectedBatchIdList
        holder.cardViewBatch.setOnClickListener(v -> {
            if (isSelectedMode) {
                itemAddToSelectedList(current.getBatch().getBatchId(), current);
                callBack.onItemsSelected(selectedBatchIdList);

                if (selectedBatchIdList.size() == 1) {
                    callBack.onSelectedItemIsOne(findStockBatchByBatchId(selectedBatchIdList.get(0)));
                }

                notifyItemChanged(position);
            }
        });


        changeSelectedItemColor(holder, current);
    }

    private void changeSelectedItemColor(ViewHolder holder, StockBatch current) {
        if (current.isSelected()) {
            holder.cardViewBatch.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red));
        } else {
            holder.cardViewBatch.setBackgroundColor(ContextCompat.getColor(context, R.color.little_dark_white));
        }
    }

    @Override
    public int getItemCount() {
        return stockBatchList.size();
    }

    //when item click itemId save to selectedList and id is equal remove from list
    private void itemAddToSelectedList(Long id, StockBatch current) {

        if (!selectedBatchIdList.contains(id)) {
            selectedBatchIdList.add(id);
            current.setSelected(true);
        } else {
            selectedBatchIdList.remove(id);
            current.setSelected(false);
        }
    }

    //if tap actionbar close button following logic work
    public void contextualActionBarClose() {
        isSelectedMode = false;
        selectedBatchIdList.clear();
        importSelectedFalse();
        notifyDataSetChanged();
    }

    public void selectAllItems() {

        boolean isAllSelected = false;

        for (StockBatch stockBatch : stockBatchList) {

            if (!stockBatch.isSelected()) {
                isAllSelected = false;
                break;
            } else {
                isAllSelected = true;
            }
        }

        if (isAllSelected) {
            importSelectedFalse();
            this.selectedBatchIdList.clear();
        } else {
            importSelectedTrue();
            insertAllBatchId();
        }
        callBack.onItemsSelected(selectedBatchIdList);

        notifyDataSetChanged();
    }

    private void insertAllBatchId() {

        selectedBatchIdList.clear();

        for (StockBatch stockBatch : stockBatchList) {
            this.selectedBatchIdList.add(stockBatch.getBatch().getBatchId());
        }
    }

    private void importSelectedTrue() {
        for (StockBatch stockBatch : stockBatchList) {
            stockBatch.setSelected(true);
        }
    }

    private StockBatch findStockBatchByBatchId(Long batchId) {
        for (StockBatch stockBatch : stockBatchList) {
            if (batchId == stockBatch.getBatch().getBatchId()) {
                return stockBatch;
            }
        }
        return null;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewBatch;
        CardView cardViewItemNameContainer;
        TextView txtStockName;
        TextView txtCostPrice;
        TextView txtSalePrice;
        TextView txtExpDate;
        TextView txtAmount;
        TextView txtNameInitialWord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewBatch = itemView.findViewById(R.id.card_view_batch);
            cardViewItemNameContainer = itemView.findViewById(R.id.card_view_item_name_container);
            txtStockName = itemView.findViewById(R.id.txt_stock_name);
            txtCostPrice = itemView.findViewById(R.id.txt_cost_price);
            txtSalePrice = itemView.findViewById(R.id.txt_sale_price);
            txtExpDate = itemView.findViewById(R.id.txt_exp_date);
            txtAmount = itemView.findViewById(R.id.txt_amount);
            txtNameInitialWord = itemView.findViewById(R.id.txt_name_initial_word);
        }
    }

    public void insertItem(List<StockBatch> stockBatchList) {
        this.stockBatchList.clear();
        this.stockBatchList.addAll(stockBatchList);
        notifyDataSetChanged();
    }

    //Assign allStockBatches isSelected property to false
    private void importSelectedFalse() {

        for (StockBatch stockBatch : stockBatchList) {

            stockBatch.setSelected(false);
        }

    }

    //for change card view color by random
    private void changeCardViewColor(CardView cardView) {
        int randomNum = AutoNumGenerator.generateNum();

        switch (randomNum) {
            case 1:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_red));
                break;
            case 2:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_green));
                break;
            case 3:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));
                break;
            case 4:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_orange));
                break;
            case 5:
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_purple));
                break;
        }
    }

    public interface CallBack {

        void onLongClicked();

        void onItemsSelected(List<Long> selectedBatchIdList);

        void onSelectedItemIsOne(StockBatch stockBatch);
    }
}
