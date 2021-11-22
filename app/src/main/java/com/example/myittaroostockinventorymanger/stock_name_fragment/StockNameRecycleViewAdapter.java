package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.local.Stock;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockNameRecycleViewAdapter extends RecyclerView.Adapter<StockNameRecycleViewAdapter.ViewHolder> {

    private List<Stock> stockList;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public StockNameRecycleViewAdapter(List<Stock> stockList) {
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

        holder.linearLayoutStockName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                callBack.onLongClickItem(v, currentStock);
                return true;
            }
        });

        holder.txtNameInitialWord.setText(initialWord);

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

        @BindView(R.id.linear_layout_stock_name)
        LinearLayout linearLayoutStockName;
        @BindView(R.id.txt_stock_name)
        TextView txtStockName;
        @BindView(R.id.txt_name_initial_word)
        TextView txtNameInitialWord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CallBack {
        void onLongClickItem(View v, Stock stock);
    }

}
