package com.project.creativly.canshopproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.model.Favorite;
import com.project.creativly.canshopproject.model.Order;

import java.util.List;


public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {

    private List<Favorite> favoriteList;
    private Context context;
    private OnItemClickListener listener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView priceTxt, orderNameTxt, dateTxt, statusTxt;
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
            layout = view.findViewById(R.id.layout);
        }
    }

    public FavouriteAdapter(Context context, List<Favorite> favoriteList, OnItemClickListener listener) {
        this.favoriteList = favoriteList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Favorite item = favoriteList.get(position);
        holder.orderNameTxt.setText(item.getTitle());
        holder.dateTxt.setText(item.getDate());
        holder.priceTxt.setText(item.getPrice());
        holder.saleImage.setImageDrawable(item.getImage());
//        holder.layout.setBackgroundColor(context.getResources().getColor(item.getColor()));
//        Drawable drawable = holder.layout.getBackground();
//        holder.progressBar.setVisibility(View.VISIBLE);
//        Picasso.get().load(item.getImage()).into(holder.saleImage, new Callback() {
//            @Override
//            public void onSuccess() {
//                holder.progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onError(Exception e) {
//                holder.progressBar.setVisibility(View.GONE);
//            }
//        });
//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onItemClick(view, position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }
}