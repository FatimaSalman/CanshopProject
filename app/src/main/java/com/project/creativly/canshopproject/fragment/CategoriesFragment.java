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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.activity.ProductDetailsActivity;
import com.project.creativly.canshopproject.adapter.CategoryAdapter;
import com.project.creativly.canshopproject.adapter.SaleAdapter;
import com.project.creativly.canshopproject.callback.CategoriesCallback;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.callback.ProductCallback;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.model.Category;
import com.project.creativly.canshopproject.model.Sales;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriesFragment extends Fragment {

    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private ConnectionManager connectionManager;
    private ProgressDialog progressDialog;
    private List<Sales> salesList = new ArrayList<>();
    private SaleAdapter saleAdapter;
    private int pageCount = 1, totalPage;
    private boolean userScrolled = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ProgressBar progressBar;
    private String slug;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        connectionManager = new ConnectionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(Objects.requireNonNull(getActivity()).getString(R.string.loading));
        progressDialog.show();
        init(view);
//        setupViewPager(viewPager);


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void init(View view) {

        RecyclerView recyclerView = view.findViewById(R.id.recycleView);
        progressBar = view.findViewById(R.id.progressBar);
        categoryAdapter = new CategoryAdapter(getActivity(), categoryList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                categoryAdapter.currentPosition = position;
                slug = categoryList.get(position).getSlug();
                categoryAdapter.notifyDataSetChanged();
                getSales(categoryList.get(position).getSlug());

//                else {
//                    getLocationCategory();
//                }
            }
        });
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);
        getCategories();

        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView recycleViewData = view.findViewById(R.id.recycleViewData);

        saleAdapter = new SaleAdapter(getActivity(), salesList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Sales sales = salesList.get(position);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("sales", sales);
                startActivity(intent);
            }
        });

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recycleViewData.setLayoutManager(gridLayoutManager);
        recycleViewData.setItemAnimator(new DefaultItemAnimator());
        recycleViewData.setNestedScrollingEnabled(false);
        recycleViewData.setAdapter(saleAdapter);
        if (categoryList.size() != 0) {
            getSales(categoryList.get(0).getSlug());
        }

        recycleViewData.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (userScrolled && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (pageCount < totalPage) {
                            progressBar.setVisibility(View.VISIBLE);
                            userScrolled = false;
                            pageCount++;
                            getSalesMore(slug);
                        }
                    }
            }
        });

    }

    public void getCategories() {
        connectionManager.getCategories(new CategoriesCallback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCategories(Category category) {
                progressDialog.dismiss();
                categoryList.add(category);
                categoryAdapter.notifyDataSetChanged();
                if (categoryList.size() == 1) {
//                    progressDialog.show();
                    getSales(categoryList.get(0).getSlug());
                }
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                AppErrorsManager.showErrorDialog(getActivity(), error);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getSales(String slug) {
       final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(Objects.requireNonNull(getActivity()).getString(R.string.loading));
        progressDialog.show();
        pageCount = 1;
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

    public void getSalesMore(String slug) {
        Log.e("count", pageCount + "");
        connectionManager.productCategories(6, pageCount, slug, new ProductCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProductDone(Sales sales) {
                salesList.add(sales);
                totalPage = pageCount + 1;
                progressBar.setVisibility(View.GONE);
                saleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                if (TextUtils.equals(error, getString(R.string.donot_have_product))) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    AppErrorsManager.showErrorDialog(getActivity(), error);
                }
            }
        });
    }


}
