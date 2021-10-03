package com.example.myittaroostockinventorymanger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.util.AutoNumGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockListRecycleViewAdapter extends RecyclerView.Adapter<StockListRecycleViewAdapter.ViewHolder> {

    private Context context;

    public StockListRecycleViewAdapter(Context context) {
        this.context = context;
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
        CardView cardView = holder.cardViewItemNameContainer;
        changeCardViewColor(cardView);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_item_name_container)
        CardView cardViewItemNameContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
}
