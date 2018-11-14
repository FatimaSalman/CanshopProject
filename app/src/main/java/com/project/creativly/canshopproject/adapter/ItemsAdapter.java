package com.project.creativly.canshopproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.model.Cart;

import java.util.List;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private List<Cart> productList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productTxt, priceTxt, quantityTxt, totalTxt;
        RelativeLayout layout;

        MyViewHolder(View view) {
            super(view);
            productTxt = view.findViewById(R.id.productTxt);
            priceTxt = view.findViewById(R.id.priceTxt);
            quantityTxt = view.findViewById(R.id.quantityTxt);
            totalTxt = view.findViewById(R.id.totalTxt);
            layout = view.findViewById(R.id.layout);
        }
    }

    public ItemsAdapter(Context context, List<Cart> productList) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Cart item = productList.get(position);
        holder.productTxt.setText(item.getTitle());
        holder.priceTxt.setText(item.getPrice());
        holder.quantityTxt.setText(item.getQuantity());
        holder.totalTxt.setText(item.getTotal() + " " + context.getString(R.string.doller_sign));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}