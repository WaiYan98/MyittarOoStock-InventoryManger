package com.example.myittaroostockinventorymanger.batch_fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.local.Stock;
import com.example.myittaroostockinventorymanger.local.StockWithBatch;
import com.example.myittaroostockinventorymanger.pojo.StockBatch;
import com.example.myittaroostockinventorymanger.util.AutoNumGenerator;
import com.example.myittaroostockinventorymanger.util.ListCreator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BatchListRecycleViewAdapter extends RecyclerView.Adapter<BatchListRecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<StockBatch> stockBatchList;
    private CallBack callBack;

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

        //when batch item onLongClicked sent current batch data to batch fragment
        holder.cardViewBatch.setOnClickListener(v -> callBack.onLongClicked(v, current));
    }

    @Override
    public int getItemCount() {
        return stockBatchList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_batch)
        CardView cardViewBatch;
        @BindView(R.id.card_view_item_name_container)
        CardView cardViewItemNameContainer;
        @BindView(R.id.txt_stock_name)
        TextView txtStockName;
        @BindView(R.id.txt_cost_price)
        TextView txtCostPrice;
        @BindView(R.id.txt_sale_price)
        TextView txtSalePrice;
        @BindView(R.id.txt_exp_date)
        TextView txtExpDate;
        @BindView(R.id.txt_amount)
        TextView txtAmount;
        @BindView(R.id.txt_name_initial_word)
        TextView txtNameInitialWord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void insertItem(List<StockBatch> stockBatchList) {
        this.stockBatchList.clear();
        this.stockBatchList.addAll(stockBatchList);
        notifyDataSetChanged();
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

        void onLongClicked(View v, StockBatch stockBatch);
    }
}
