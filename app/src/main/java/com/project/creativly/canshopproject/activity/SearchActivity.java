package com.project.creativly.canshopproject.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.adapter.SaleAdapter;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.callback.ProductCallback;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.manager.FontManager;
import com.project.creativly.canshopproject.model.Sales;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<Sales> salesList = new ArrayList<>();
    private SaleAdapter saleAdapter;
    private ConnectionManager connectionManager;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        connectionManager = new ConnectionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        init();
    }

    public void init() {
        RelativeLayout backLayout = findViewById(R.id.backLayout);
        final EditText searchEditText = findViewById(R.id.searchEditText);
        ImageView searchIcon = findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSales(searchEditText.getText().toString().trim());
                FontManager.hideKeyboard(SearchActivity.this);
            }
        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView salesRecyclerView = findViewById(R.id.salesRecyclerView);
        saleAdapter = new SaleAdapter(this, salesList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Sales sales = salesList.get(position);
                Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                intent.putExtra("sales", sales);
                startActivity(intent);
            }
        });
        salesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        salesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        salesRecyclerView.setNestedScrollingEnabled(false);
        salesRecyclerView.setAdapter(saleAdapter);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getSales(v.getText().toString());
                    FontManager.hideKeyboard(SearchActivity.this);
                    return true;
                }
                return false;
            }
        });
    }

    public void getSales(String search) {
        salesList.clear();
        progressDialog.show();
        connectionManager.search(search, new ProductCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProductDone(Sales sales) {
                progressDialog.dismiss();
                salesList.add(sales);
                saleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                AppErrorsManager.showErrorDialog(SearchActivity.this, error);
            }
        });
    }

}
