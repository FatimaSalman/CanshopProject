package com.project.creativly.canshopproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.project.creativly.canshopproject.model.Category;
import com.project.creativly.canshopproject.model.Favorite;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private OnItemClickListener listener;
    public int currentPosition;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTxt;
        RelativeLayout layout;
//        View viewLine;

        MyViewHolder(View view) {
            super(view);
            itemNameTxt = view.findViewById(R.id.itemNameTxt);
//            viewLine = view.findViewById(R.id.view);
            layout = view.findViewById(R.id.layout);
        }
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Category item = categoryList.get(position);
        holder.itemNameTxt.setText(item.getName());
        if (currentPosition != position) {
//            holder.viewLine.setVisibility(View.GONE);
            holder.itemNameTxt.setTextColor(context.getResources().getColor(R.color.colorEnd));
            holder.itemNameTxt.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
//            holder.viewLine.setVisibility(View.VISIBLE);
            holder.itemNameTxt.setTextColor(context.getResources().getColor(R.color.colorStart));
            holder.itemNameTxt.setBackgroundColor(context.getResources().getColor(R.color.colorEnd));
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
        return categoryList.size();
    }
}