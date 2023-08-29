package com.example.myittaroostockinventorymanger.ui.item_name;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.data.entities.Stock;

import java.util.ArrayList;
import java.util.List;
public class StockNameRecycleViewAdapter extends RecyclerView.Adapter<StockNameRecycleViewAdapter.ViewHolder> {

    private List<Stock> stockList;
    private Context context;
    private CallBack callBack;
    private boolean isSelectedMode = false;
    private List<Long> selectedStockIdList = new ArrayList<>();

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public StockNameRecycleViewAdapter(Context context, List<Stock> stockList) {
        this.context = context;
        this.stockList = stockList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_stock_name, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stock currentStock = stockList.get(position);
        String initialWord = Character.toString(currentStock.getName().charAt(0)).
                toUpperCase();

        holder.txtStockName.setText(currentStock.getName());

        holder.linearLayoutStockName.setOnLongClickListener(v -> {
            isSelectedMode = true;
            callBack.onLongClickItem();
            return true;
        });

        holder.linearLayoutStockName.setOnClickListener(v -> {
            if (isSelectedMode) {
                //For test
                itemAddToSelectedList(currentStock);
                callBack.onClickItem(selectedStockIdList);

                if (selectedStockIdList.size() == 1) {
                    callBack.onSelectedItemIsOne(currentStock);
                }

                notifyItemChanged(position);
            }
        });

        holder.txtNameInitialWord.setText(initialWord);

        stockBackgroundColorChange(currentStock, holder);
    }

    private void itemAddToSelectedList(Stock currentStock) {

        long stockId = currentStock.getStockId();

        if (!selectedStockIdList.contains(stockId)) {
            currentStock.setSelected(true);
            selectedStockIdList.add(stockId);
        } else {
            currentStock.setSelected(false);
            selectedStockIdList.remove(stockId);
        }

    }

    public void onClickSelectAll() {

        Boolean isSelectedAll = false;

        for (Stock stock : stockList) {

            if (stock.isSelected()) {
                isSelectedAll = true;
            } else {
                isSelectedAll = false;
                break;
            }
        }

        if (isSelectedAll) {
            selectedStockIdList.clear();
            importSelectedFalse();
        } else {
            insertAllStockIds();
            importSelectedTrue();
        }
        callBack.onClickItem(selectedStockIdList);
        notifyDataSetChanged();
    }

    private void insertAllStockIds() {

        selectedStockIdList.clear();

        for (Stock stock : stockList) {
            selectedStockIdList.add(stock.getStockId());
        }
    }

    public void contextualActionBarClose() {
        selectedStockIdList.clear();
        isSelectedMode = false;
        importSelectedFalse();
        notifyDataSetChanged();
    }

    private void importSelectedFalse() {
        for (Stock stock : stockList) {
            stock.setSelected(false);
        }
    }

    private void importSelectedTrue() {
        for (Stock stock : stockList) {
            stock.setSelected(true);
        }
    }

    private void stockBackgroundColorChange(Stock stock, ViewHolder holder) {

        if (stock.isSelected()) {
            holder.cardViewStock.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red));
        } else {
            holder.cardViewStock.setBackgroundColor(ContextCompat.getColor(context, R.color.little_dark_white));
        }
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    /**
     * @param stockList To update data by observer
     */
    public void insertItem(List<Stock> stockList) {
        this.stockList.clear();
        this.stockList.addAll(stockList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayoutStockName;
        CardView cardViewStock;
        TextView txtStockName;
        TextView txtNameInitialWord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutStockName = itemView.findViewById(R.id.linear_layout_stock_name);
            cardViewStock = itemView.findViewById(R.id.card_view_stock);
            txtStockName = itemView.findViewById(R.id.txt_stock_name);
            txtNameInitialWord = itemView.findViewById(R.id.txt_name_initial_word);
        }
    }

    public interface CallBack {

        void onLongClickItem();

        void onClickItem(List<Long> selectedStockIdList);

        void onSelectedItemIsOne(Stock stock);
    }

}
