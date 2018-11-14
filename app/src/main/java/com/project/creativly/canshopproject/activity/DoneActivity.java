package com.project.creativly.canshopproject.activity;


import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.adapter.ProductAdapter;
import com.project.creativly.canshopproject.database.DBHelper;
import com.project.creativly.canshopproject.model.Cart;
import com.project.creativly.canshopproject.model.OrderDetails;

import java.util.ArrayList;
import java.util.List;

public class DoneActivity extends AppCompatActivity implements View.OnClickListener {
    private ProductAdapter productAdapter;
    private List<Cart> cartList = new ArrayList<>();
    private OrderDetails orderDetails;
    private DBHelper dbHelper;
    private TextView amountTxt, priceTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_done);
        dbHelper = new DBHelper(this);
        orderDetails = (OrderDetails) getIntent().getSerializableExtra("orderDetails");
        init();
    }

    @SuppressLint("SetTextI18n")
    public void init() {
        amountTxt = findViewById(R.id.amountTxt);
        priceTxt = findViewById(R.id.priceTxt);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        productAdapter = new ProductAdapter(this, cartList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(productAdapter);
        getCartItem();

        RelativeLayout backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        TextView buyerTxt = findViewById(R.id.buyerTxt);

        TextView buyerAddressTxt = findViewById(R.id.buyerAddressTxt);
        TextView buyerMobileTxt = findViewById(R.id.buyerMobileTxt);
        TextView dateTxt = findViewById(R.id.dateTxt);
        buyerTxt.setText(orderDetails.getFirstName() + " " + orderDetails.getLastName());
        Log.e("address2", orderDetails.getAddress_2());
        if (!orderDetails.getAddress_2().isEmpty()) {
            buyerAddressTxt.setText(orderDetails.getAddress_1() + " - " + orderDetails.getAddress_2() + " - " + orderDetails.getCity());
        } else {
            buyerAddressTxt.setText(orderDetails.getAddress_1() + " - " + orderDetails.getCity());
        }
        buyerMobileTxt.setText(orderDetails.getPhone());
        dateTxt.setText(orderDetails.getCreated_at());

    }

    @SuppressLint("SetTextI18n")
    public void getCartItem() {
        cartList.clear();
        float allTotal = 0;
        List<Cart> cartDBList = dbHelper.getAllCart();
        for (Cart cart : cartDBList) {
            cartList.add(cart);
            productAdapter.notifyDataSetChanged();
            if (cart.getPrice().endsWith("د.ك")) {
                String price = cart.getPrice().replace("د.ك", "");
                float multi = Float.parseFloat(price) * Float.parseFloat(cart.getQuantity());
                allTotal += multi;
            } else if (cart.getPrice().endsWith("K.D")) {
                String price = cart.getPrice().replace("K.D", "");
                float multi = Float.parseFloat(price) * Float.parseFloat(cart.getQuantity());
                allTotal += multi;
            }

        }

        amountTxt.setText(String.valueOf(allTotal) + " " + getString(R.string.doller_sign));
        priceTxt.setText(String.valueOf(allTotal + 1) + " " + getString(R.string.doller_sign));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backLayout) {
            finish();
        } else if (id == R.id.nextButton) {
            DBHelper dbHelper = new DBHelper(this);
            for (Cart cart : dbHelper.getAllCart()) {
                dbHelper.deleteCart(cart.getId());
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("count", dbHelper.getCartCount());
            startActivity(intent);
            finish();
        }
    }
}
