package com.project.creativly.canshopproject.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.activity.DoneActivity;
import com.project.creativly.canshopproject.activity.OrderDetailsActivity;
import com.project.creativly.canshopproject.activity.ProfileActivity;
import com.project.creativly.canshopproject.adapter.OfferAdapter;
import com.project.creativly.canshopproject.adapter.OrderAdapter;
import com.project.creativly.canshopproject.adapter.SaleAdapter;
import com.project.creativly.canshopproject.callback.LoginCallback;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.callback.OrderCallback;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.model.Offer;
import com.project.creativly.canshopproject.model.Order;
import com.project.creativly.canshopproject.model.Sales;
import com.project.creativly.canshopproject.model.User;

import java.util.ArrayList;
import java.util.List;

public class MyOrderFragment extends Fragment {


    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private ConnectionManager connectionManager;
    private ProgressDialog progressDialog;
    private TextView productNoTxt, noDataTxt;
    private RelativeLayout headLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        connectionManager = new ConnectionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        init(view);
        return view;
    }

    public void init(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        headLayout = view.findViewById(R.id.headLayout);
        noDataTxt = view.findViewById(R.id.noDataTxt);
        productNoTxt = view.findViewById(R.id.productNoTxt);
        orderAdapter = new OrderAdapter(getActivity(), orderList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                intent.putExtra("order_id", orderList.get(position).getId());
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderAdapter);
        getOrder();
    }

    public void getOrder() {
        connectionManager.orderList(new OrderCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onOrderDone(Order order) {
                progressDialog.dismiss();
                if (order.getId() != null) {
                    orderList.add(order);
                    productNoTxt.setText(orderList.size() + "");
                    headLayout.setVisibility(View.VISIBLE);
                    noDataTxt.setVisibility(View.GONE);
                    orderAdapter.notifyDataSetChanged();
                } else {
                    headLayout.setVisibility(View.VISIBLE);
                    noDataTxt.setVisibility(View.VISIBLE);
                    productNoTxt.setText(orderList.size() + "");
                    Log.e("noOrder", "noOrder");
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                AppErrorsManager.showErrorDialog(getActivity(), error);
            }
        });
    }
}
