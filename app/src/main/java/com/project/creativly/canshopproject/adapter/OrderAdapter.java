package com.project.creativly.canshopproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.manager.AppLanguage;
import com.project.creativly.canshopproject.manager.AppPreferences;
import com.project.creativly.canshopproject.model.Offer;
import com.project.creativly.canshopproject.model.Order;

import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<Order> orderList;
    private Context context;
    private OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView priceTxt, orderNameTxt, dateTxt, statusTxt, orderTxt;
        ProgressBar progressBar;
        RoundedImageView saleImage;
        RelativeLayout layout;

        MyViewHolder(View view) {
            super(view);
            priceTxt = view.findViewById(R.id.priceTxt);
            dateTxt = view.findViewById(R.id.dateTxt);
            orderNameTxt = view.findViewById(R.id.orderNameTxt);
            statusTxt = view.findViewById(R.id.statusTxt);
            progressBar = view.findViewById(R.id.progressBar);
            saleImage = view.findViewById(R.id.saleImage);
            orderTxt = view.findViewById(R.id.orderTxt);
            layout = view.findViewById(R.id.layout);
        }
    }

    public OrderAdapter(Context context, List<Order> orderList, OnItemClickListener listener) {
        this.orderList = orderList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Order item = orderList.get(position);
        holder.orderTxt.setText(item.getId());
        if (TextUtils.equals(AppLanguage.getLanguage(context), "ar")) {
            holder.statusTxt.setText(item.getStatus_ar());
        } else {
            holder.statusTxt.setText(item.getStatus_ar());
        }
        holder.dateTxt.setText(item.getDate());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}