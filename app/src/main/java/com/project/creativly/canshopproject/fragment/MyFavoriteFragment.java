package com.project.creativly.canshopproject.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.adapter.FavouriteAdapter;
import com.project.creativly.canshopproject.adapter.OfferAdapter;
import com.project.creativly.canshopproject.adapter.OrderAdapter;
import com.project.creativly.canshopproject.adapter.SaleAdapter;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.model.Favorite;
import com.project.creativly.canshopproject.model.Offer;
import com.project.creativly.canshopproject.model.Order;
import com.project.creativly.canshopproject.model.Sales;

import java.util.ArrayList;
import java.util.List;

public class MyFavoriteFragment extends Fragment {


    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private List<Favorite> favoriteList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favorite, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        favouriteAdapter = new FavouriteAdapter(getActivity(), favoriteList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(getActivity(), OfferDetailsActivity.class);
//                intent.putExtra("offer_id", salesList.get(position).getId());
//                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(favouriteAdapter);
        getSales();

    }

    public void getSales() {
        Favorite favorite = new Favorite("1", "30", "18 Apr 2018", "shoes Sleeve Shirt",
                getResources().getDrawable(R.drawable.img_2));
        favoriteList.add(favorite);
        favorite = new Favorite("1", "30", "18 Apr 2018", "shoes Sleeve Shirt",
                getResources().getDrawable(R.drawable.img_2));
        favoriteList.add(favorite);
        favorite = new Favorite("1", "30", "18 Apr 2018", "shoes Sleeve Shirt",
                getResources().getDrawable(R.drawable.img_2));
        favoriteList.add(favorite);
        favorite = new Favorite("1", "30", "18 Apr 2018", "shoes Sleeve Shirt",
                getResources().getDrawable(R.drawable.img_2));
        favoriteList.add(favorite);
        favorite = new Favorite("1", "30", "18 Apr 2018", "shoes Sleeve Shirt",
                getResources().getDrawable(R.drawable.img_2));
        favoriteList.add(favorite);

    }
}
