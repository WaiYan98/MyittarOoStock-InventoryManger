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
import com.example.myittaroostockinventorymanger.data.entities.Item;

import java.util.ArrayList;
import java.util.List;
public class ItemNameRecycleViewAdapter extends RecyclerView.Adapter<ItemNameRecycleViewAdapter.ViewHolder> {

    private List<Item> itemList;
    private Context context;
    private CallBack callBack;
    private boolean isSelectedMode = false;
    private List<Long> selectedStockIdList = new ArrayList<>();

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public ItemNameRecycleViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
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
        Item currentItem = itemList.get(position);
        String initialWord = Character.toString(currentItem.getName().charAt(0)).
                toUpperCase();

        holder.txtStockName.setText(currentItem.getName());

        holder.linearLayoutStockName.setOnLongClickListener(v -> {
            isSelectedMode = true;
            callBack.onLongClickItem();
            return true;
        });

        holder.linearLayoutStockName.setOnClickListener(v -> {
            if (isSelectedMode) {
                //For test
                itemAddToSelectedList(currentItem);
                callBack.onClickItem(selectedStockIdList);

                if (selectedStockIdList.size() == 1) {
                    callBack.onSelectedItemIsOne(currentItem);
                }

                notifyItemChanged(position);
            }
        });

        holder.txtNameInitialWord.setText(initialWord);

        stockBackgroundColorChange(currentItem, holder);
    }

    private void itemAddToSelectedList(Item currentItem) {

        long stockId = currentItem.getItemId();

        if (!selectedStockIdList.contains(stockId)) {
            currentItem.setSelected(true);
            selectedStockIdList.add(stockId);
        } else {
            currentItem.setSelected(false);
            selectedStockIdList.remove(stockId);
        }

    }

    public void onClickSelectAll() {

        Boolean isSelectedAll = false;

        for (Item item : itemList) {

            if (item.isSelected()) {
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

        for (Item item : itemList) {
            selectedStockIdList.add(item.getItemId());
        }
    }

    public void contextualActionBarClose() {
        selectedStockIdList.clear();
        isSelectedMode = false;
        importSelectedFalse();
        notifyDataSetChanged();
    }

    private void importSelectedFalse() {
        for (Item item : itemList) {
            item.setSelected(false);
        }
    }

    private void importSelectedTrue() {
        for (Item item : itemList) {
            item.setSelected(true);
        }
    }

    private void stockBackgroundColorChange(Item item, ViewHolder holder) {

        if (item.isSelected()) {
            holder.cardViewStock.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red));
        } else {
            holder.cardViewStock.setBackgroundColor(ContextCompat.getColor(context, R.color.little_dark_white));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * @param itemList To update data by observer
     */
    public void insertItem(List<Item> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
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

        void onSelectedItemIsOne(Item item);
    }

}
