package com.project.creativly.canshopproject.fragment;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.activity.ProductDetailsActivity;
import com.project.creativly.canshopproject.adapter.OfferAdapter;
import com.project.creativly.canshopproject.adapter.SaleAdapter;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.callback.ProductCallback;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.model.Offer;
import com.project.creativly.canshopproject.model.Sales;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProductsFragment extends Fragment {

    private List<Sales> salesList = new ArrayList<>();
    private SaleAdapter saleAdapter;
    private ConnectionManager connectionManager;
    private ProgressDialog progressDialog;
    private int pageCount = 1, totalPage;
    private boolean userScrolled = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ProgressBar progressBar;
    private String slug;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        connectionManager = new ConnectionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(Objects.requireNonNull(getActivity()).getString(R.string.loading));
        progressDialog.show();
        init(view);
        Bundle args = getArguments();
        assert args != null;
        slug = args.getString("slug");
        Log.e("slug", slug);
        return view;
    }

    public void init(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView salesRecyclerView = view.findViewById(R.id.salesRecyclerView);

        saleAdapter = new SaleAdapter(getActivity(), salesList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Sales sales = salesList.get(position);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("sales", sales);
                startActivity(intent);
            }
        });
//        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        salesRecyclerView.setLayoutManager(gridLayoutManager);
        salesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        salesRecyclerView.setNestedScrollingEnabled(false);
        salesRecyclerView.setAdapter(saleAdapter);
        getSales();

        salesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = gridLayoutManager.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                if (dy > 0)
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (pageCount < totalPage) {
                            progressBar.setVisibility(View.VISIBLE);
                            userScrolled = false;
                            pageCount++;
                            getSalesMore();
                        }
                    }
            }
        });
    }

    public void getSales() {
//        progressDialog.show();
        Log.e("count", pageCount + "");
        salesList.clear();
        connectionManager.productCategories(6, pageCount, slug, new ProductCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProductDone(Sales sales) {
                progressDialog.dismiss();
                salesList.add(sales);
                totalPage = pageCount + 1;
                progressBar.setVisibility(View.GONE);
                saleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                AppErrorsManager.showErrorDialog(getActivity(), error);
            }
        });
    }

    public void getSalesMore() {
//        progressDialog.show();
        Log.e("count", pageCount + "");
//        salesList.clear();
        connectionManager.productCategories(6, pageCount, slug, new ProductCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProductDone(Sales sales) {
                progressDialog.dismiss();
                salesList.add(sales);
                totalPage = pageCount + 1;
                progressBar.setVisibility(View.GONE);
                saleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                AppErrorsManager.showErrorDialog(getActivity(), error);
            }
        });
    }


}
