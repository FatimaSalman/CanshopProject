package com.project.creativly.canshopproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.project.creativly.canshopproject.model.Cart;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<Cart> cartList;
    private Context context;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView priceTxt, orderNameTxt, dateTxt;
        public TextView countTxt;
        ProgressBar progressBar;
        RoundedImageView saleImage;
        RelativeLayout layout;
        ImageView ic_plus, ic_minus, deleteImg;

        MyViewHolder(View view) {
            super(view);
            priceTxt = view.findViewById(R.id.priceTxt);
            dateTxt = view.findViewById(R.id.dateTxt);
            dateTxt.setVisibility(View.GONE);
            orderNameTxt = view.findViewById(R.id.orderNameTxt);
            countTxt = view.findViewById(R.id.countTxt);
            progressBar = view.findViewById(R.id.progressBar);
            saleImage = view.findViewById(R.id.saleImage);
            ic_plus = view.findViewById(R.id.ic_plus);
            deleteImg = view.findViewById(R.id.deleteImg);
            ic_minus = view.findViewById(R.id.ic_minus);
            layout = view.findViewById(R.id.layout);
        }
    }

    public CartAdapter(Context context, List<Cart> cartList, OnItemClickListener listener) {
        this.cartList = cartList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Cart item = cartList.get(position);
        holder.orderNameTxt.setText(item.getTitle());
        holder.dateTxt.setText(item.getDate());
        holder.priceTxt.setText(item.getPrice());
        holder.countTxt.setText(item.getQuantity());
//        holder.saleImage.setImageDrawable(item.getImage());
//        holder.layout.setBackgroundColor(context.getResources().getColor(item.getColor()));
//        Drawable drawable = holder.layout.getBackground();
        holder.progressBar.setVisibility(View.VISIBLE);
//        String imageUrl = item.getImage().replace("http://canshop20-com.stackstaging.com", "http://canshop.net");
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
        holder.ic_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position);
            }
        });
        holder.ic_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position);
            }
        });
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}