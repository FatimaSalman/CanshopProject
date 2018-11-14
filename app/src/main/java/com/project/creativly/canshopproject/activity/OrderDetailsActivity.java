package com.project.creativly.canshopproject.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.adapter.ItemsAdapter;
import com.project.creativly.canshopproject.adapter.ProductAdapter;
import com.project.creativly.canshopproject.callback.OrderDetailsCallback;
import com.project.creativly.canshopproject.database.DBHelper;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.model.Cart;
import com.project.creativly.canshopproject.model.OrderDetails;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ItemsAdapter productAdapter;
    private List<Cart> cartList = new ArrayList<>();
    private String order_id;
    private TextView amountTxt, priceTxt, buyerTxt, buyerAddressTxt, buyerMobileTxt, dateTxt;
    private ConnectionManager connectionManager;
    private ScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_done);
        connectionManager = new ConnectionManager(this);
        order_id = getIntent().getStringExtra("order_id");
        Log.e("order_id", order_id);
        init();
    }

    public void init() {
        amountTxt = findViewById(R.id.amountTxt);
        scrollView = findViewById(R.id.scrollView);
        scrollView.setVisibility(View.GONE);
        priceTxt = findViewById(R.id.priceTxt);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RelativeLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setVisibility(View.GONE);
        productAdapter = new ItemsAdapter(this, cartList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(productAdapter);
//        getCartItem();

        RelativeLayout backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setVisibility(View.GONE);
        buyerTxt = findViewById(R.id.buyerTxt);
        buyerAddressTxt = findViewById(R.id.buyerAddressTxt);
        buyerMobileTxt = findViewById(R.id.buyerMobileTxt);
        dateTxt = findViewById(R.id.dateTxt);
        orderShow();
    }

//    @SuppressLint("SetTextI18n")
//    public void getCartItem() {
//        cartList.clear();
//        float allTotal = 0;
//        List<Cart> cartDBList = dbHelper.getAllCart();
//        for (Cart cart : cartDBList) {
//            cartList.add(cart);
//            productAdapter.notifyDataSetChanged();
//            if (cart.getPrice().endsWith("د.ك")) {
//                String price = cart.getPrice().replace("د.ك", "");
//                float multi = Float.parseFloat(price) * Float.parseFloat(cart.getQuantity());
//                allTotal += multi;
//            } else if (cart.getPrice().endsWith("K.D")) {
//                String price = cart.getPrice().replace("K.D", "");
//                float multi = Float.parseFloat(price) * Float.parseFloat(cart.getQuantity());
//                allTotal += multi;
//            }
//
//        }
//
//        amountTxt.setText(allTotal + " " + getString(R.string.doller_sign));
//        priceTxt.setText(String.valueOf(allTotal) + " " + getString(R.string.doller_sign));
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backLayout) {
            finish();
        }
    }

    public void orderShow() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        connectionManager.orderListShow(order_id, new OrderDetailsCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onOrderDone(OrderDetails orderDetails) {
                progressDialog.dismiss();
                scrollView.setVisibility(View.VISIBLE);
                buyerTxt.setText(orderDetails.getFirstName() + " " + orderDetails.getLastName());
                Log.e("address2", orderDetails.getAddress_2());
                if (!orderDetails.getAddress_2().isEmpty()) {
                    buyerAddressTxt.setText(orderDetails.getAddress_1() + " - " +
                            orderDetails.getAddress_2() + " - " + orderDetails.getCity());
                } else {
                    buyerAddressTxt.setText(orderDetails.getAddress_1() + " - " +
                            orderDetails.getCity());
                }
                buyerMobileTxt.setText(orderDetails.getPhone());
                dateTxt.setText(orderDetails.getCreated_at());
                amountTxt.setText(orderDetails.getTotal() + " " + getString(R.string.doller_sign));
                float total = Float.parseFloat(orderDetails.getTotal());
                priceTxt.setText((total + 1) + " " + getString(R.string.doller_sign));
                for (Cart cart : orderDetails.getCartList()) {
                    cartList.add(cart);
                    productAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                AppErrorsManager.showErrorDialog(OrderDetailsActivity.this, error);
            }
        });
    }
}
