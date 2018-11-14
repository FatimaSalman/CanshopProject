package com.project.creativly.canshopproject.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.adapter.CartAdapter;
import com.project.creativly.canshopproject.callback.OnItemClickListener;
import com.project.creativly.canshopproject.database.DBHelper;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList = new ArrayList<>();
    private CartAdapter.MyViewHolder myViewHolder;
    private int total;
    private DBHelper dbHelper;
    private TextView priceTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dbHelper = new DBHelper(this);
        init();
    }

    public void init() {
        recyclerView = findViewById(R.id.recyclerView);
        priceTxt = findViewById(R.id.priceTxt);
        cartAdapter = new CartAdapter(this, cartList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                myViewHolder =
                        (CartAdapter.MyViewHolder) recyclerView.findViewHolderForLayoutPosition(position);
                final Cart cart = cartList.get(position);
                int id = view.getId();
                String txt = myViewHolder.countTxt.getText().toString();
                total = Integer.parseInt(txt);
                if (id == R.id.ic_plus) {
                    total = total + 1;
                    myViewHolder.countTxt.setText(String.valueOf(total));
                    Cart cart1 = new Cart(cart.getId(), cart.getImage(), cart.getPrice(),
                            String.valueOf(total), cart.getDate(), cart.getTitle());
                    dbHelper.updateCart(cart1);
                    getCartItem();
                } else if (id == R.id.ic_minus) {
                    if (total > 0) {
                        total = total - 1;
                        myViewHolder.countTxt.setText(String.valueOf(total));
                        Cart cart1 = new Cart(cart.getId(), cart.getImage(), cart.getPrice(),
                                String.valueOf(total), cart.getDate(), cart.getTitle());
                        dbHelper.updateCart(cart1);
                        getCartItem();
                    }
                } else if (id == R.id.deleteImg) {
                    AppErrorsManager.showSuccessDialog(CartActivity.this, getString(R.string.delet_product),
                            getString(R.string.do_you_need_to_delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dbHelper.deleteCart(cart.getId());
                                    getCartItem();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);
        getCartItem();
        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
        RelativeLayout backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
    }

    public void getCartItem() {
        cartList.clear();
        float allTotal = 0;
        List<Cart> cartDBList = dbHelper.getAllCart();
        for (Cart cart : cartDBList) {
            cartList.add(cart);
            cartAdapter.notifyDataSetChanged();
            if (cart.getPrice().endsWith("د.ك")) {
                String price = cart.getPrice().replace("د.ك", "");
                float multi = Float.parseFloat(price) * Float.parseFloat(cart.getQuantity());
                allTotal += multi;
            }else{
                String price = cart.getPrice().replace("K.D", "");
                float multi = Float.parseFloat(price) * Float.parseFloat(cart.getQuantity());
                allTotal += multi;
            }

        }

        priceTxt.setText(String.valueOf(allTotal));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backLayout) {
            finish();
        } else if (id == R.id.nextButton) {
            Intent intent = new Intent(this, AddressActivity.class);
            startActivity(intent);
        }
    }
}
