package com.project.creativly.canshopproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.project.creativly.canshopproject.manager.FontManager;
import com.project.creativly.canshopproject.model.Sales;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.MyViewHolder> {

    private List<Sales> salesList;
    private Context context;
    private OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView priceTxt, salesNameTxt, typeTxt;
        ProgressBar progressBar;
        RoundedImageView saleImage;
        RelativeLayout layout;
        ImageView outImg;

        MyViewHolder(View view) {
            super(view);
            priceTxt = view.findViewById(R.id.priceTxt);
            typeTxt = view.findViewById(R.id.typeTxt);
            typeTxt.setVisibility(View.GONE);
            salesNameTxt = view.findViewById(R.id.salesNameTxt);
            progressBar = view.findViewById(R.id.progressBar);
            saleImage = view.findViewById(R.id.saleImage);
            layout = view.findViewById(R.id.layout);
            outImg = view.findViewById(R.id.outImg);
        }
    }

    public SaleAdapter(Context context, List<Sales> salesList, OnItemClickListener listener) {
        this.salesList = salesList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Sales item = salesList.get(position);

        if (TextUtils.equals(item.getStock_status(), "false")) {
            holder.outImg.setVisibility(View.GONE);
        } else {
            holder.outImg.setVisibility(View.VISIBLE);
        }

        holder.salesNameTxt.setText(item.getName());
        holder.priceTxt.setText(item.getPrice());
        holder.typeTxt.setText(FontManager.getTimeMilliSec(item.getDate()));
        if (TextUtils.equals(item.getImage(), "false")) {
            holder.saleImage.setImageDrawable(context.getResources().getDrawable(R.drawable.product_img));
        } else {
            holder.progressBar.setVisibility(View.VISIBLE);
//            String imageUrl = item.getImage().replace("http://canshop20-com.stackstaging.com", "http://canshop.net");
            Picasso.with(context).load(item.getImage()).into(holder.saleImage, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    holder.progressBar.setVisibility(View.GONE);
                }
            });
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }
}