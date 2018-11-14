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
import com.project.creativly.canshopproject.activity.SearchActivity;
import com.project.creativly.canshopproject.adapter.OfferAdapter;
import com.project.creativly.canshopproject.adapter.SaleAdapter;
import com.project.creativly.canshopproject.callback.OfferCallback;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.callback.ProductCallback;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.model.Offer;
import com.project.creativly.canshopproject.model.Sales;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private List<Sales> salesList = new ArrayList<>();
    private List<Offer> offerList = new ArrayList<>();
    private SaleAdapter saleAdapter;
    private OfferAdapter offerAdapter;
    private ConnectionManager connectionManager;
    private ProgressDialog progressDialog;
    private TextView productNoTxt, offerTxt;
    private RelativeLayout saleLayout;
    private int pageCount = 1, totalPage;
    private boolean userScrolled = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ProgressBar progressBar, progressBar1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        connectionManager = new ConnectionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(Objects.requireNonNull(getActivity()).getString(R.string.loading));
        progressDialog.show();
        init(view);
        return view;
    }

    public void init(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        progressBar1 = view.findViewById(R.id.progressBar1);
        RecyclerView salesRecyclerView = view.findViewById(R.id.salesRecyclerView);
        saleLayout = view.findViewById(R.id.saleLayout);
        productNoTxt = view.findViewById(R.id.productNoTxt);
        offerTxt = view.findViewById(R.id.offerTxt);
        RecyclerView offerRecyclerView = view.findViewById(R.id.offerRecyclerView);
        TextView searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.setOnClickListener(this);
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

        offerAdapter = new OfferAdapter(getActivity(), offerList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Offer offer = offerList.get(position);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("offers", offer);
                startActivity(intent);
            }
        });
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        offerRecyclerView.setLayoutManager(linearLayoutManager);
        offerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        offerRecyclerView.setAdapter(offerAdapter);
        getOffer();

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
                            getSales();
                        }
                    }
            }
        });
    }

    public void getSales() {
        Log.e("count", pageCount + "");
//        salesList.clear();
        connectionManager.productLatest(6, pageCount, new ProductCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProductDone(Sales sales) {
                progressDialog.dismiss();
                salesList.add(sales);
                totalPage = pageCount + 1;
                saleLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                productNoTxt.setText(salesList.size() + "");
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

    public void getOffer() {
//        offerList.clear();
        connectionManager.productOffers(6, pageCount, new OfferCallback() {
            @Override
            public void onOfferDone(Offer offer) {
                progressDialog.dismiss();
                offerList.add(offer);
                offerTxt.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.GONE);
                offerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                progressBar1.setVisibility(View.GONE);
                AppErrorsManager.showErrorDialog(getActivity(), error);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.searchEditText) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        }
    }
}
