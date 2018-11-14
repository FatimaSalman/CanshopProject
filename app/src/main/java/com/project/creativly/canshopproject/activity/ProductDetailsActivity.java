package com.project.creativly.canshopproject.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.database.DBHelper;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.model.Cart;
import com.project.creativly.canshopproject.model.Offer;
import com.project.creativly.canshopproject.model.Sales;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Sales sales;
    private Offer offer;
    private TextView countTxt, countTxt_;
    private DBHelper dbHelper;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        dbHelper = new DBHelper(this);
        sales = (Sales) getIntent().getSerializableExtra("sales");
        offer = (Offer) getIntent().getSerializableExtra("offers");
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void init() {
        RelativeLayout cartLayout = findViewById(R.id.cartLayout);
        RelativeLayout backLayout = findViewById(R.id.backLayout);
        RelativeLayout addLayout = findViewById(R.id.addLayout);
        backLayout.setOnClickListener(this);
        addLayout.setOnClickListener(this);
        cartLayout.setOnClickListener(this);
        ImageView header_image = findViewById(R.id.header_image);
        TextView productNameTxt = findViewById(R.id.productNameTxt);
        TextView productTxt = findViewById(R.id.productTxt);
        TextView priceTxt = findViewById(R.id.priceTxt);
        TextView productDetails = findViewById(R.id.productDetails);
        if (sales != null) {
            Picasso.with(this).load(sales.getImage()).into(header_image);
            productNameTxt.setText(sales.getName());
            productTxt.setText(sales.getName());
            priceTxt.setText(sales.getPrice());
            productDetails.setText(Html.fromHtml(sales.getDescription()));
        } else {
            Picasso.with(this).load(offer.getImage()).into(header_image);
            productNameTxt.setText(offer.getName());
            productTxt.setText(offer.getName());
            priceTxt.setText(offer.getPrice());
            productDetails.setText(Html.fromHtml(offer.getDescription()));
        }
        countTxt = findViewById(R.id.countTxt);
        countTxt_ = findViewById(R.id.countTxt_);
        ImageView ic_plus = findViewById(R.id.ic_plus);
        ic_plus.setOnClickListener(this);
        ImageView ic_minus = findViewById(R.id.ic_minus);
        ic_minus.setOnClickListener(this);

        if (dbHelper.getCartCount() == 0) {
            countTxt_.setVisibility(View.GONE);
        } else {
            countTxt_.setVisibility(View.VISIBLE);
            countTxt_.setText(String.valueOf(dbHelper.getCartCount()));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        String count = countTxt.getText().toString().trim();
        int total = Integer.parseInt(count);
        int id = view.getId();
        if (id == R.id.backLayout) {
            finish();
        } else if (id == R.id.ic_plus) {
            total = total + 1;
            countTxt.setText(total + "");
        } else if (id == R.id.ic_minus) {
            if (total > 1) {
                total = total - 1;
                countTxt.setText(total + "");
            }
        } else if (id == R.id.addLayout) {
            if (offer != null) {
                if (TextUtils.equals(offer.getStock_status(), "false")) {
                    AppErrorsManager.showSuccessDialog(this, getString(R.string.add_to_cart), getString(R.string.do_you_need_add),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addToCart();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                } else {
                    AppErrorsManager.showErrorDialog(this, getString(R.string.this_product_is_out_stock));
                }
            } else if (sales != null) {
                if (TextUtils.equals(sales.getStock_status(), "false")) {
                    AppErrorsManager.showSuccessDialog(this, getString(R.string.add_to_cart), getString(R.string.do_you_need_add),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addToCart();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                }else{
                    AppErrorsManager.showErrorDialog(this, getString(R.string.this_product_is_out_stock));
                }
            }

        } else if (id == R.id.cartLayout) {
            if (dbHelper.getCartCount() == 0) {
                AppErrorsManager.showSuccessDialog(this, getString(R.string.your_cart_not_has_any_product));
            } else {
                Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        }
    }

    public void addToCart() {
        String quantity = countTxt.getText().toString().trim();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String date = format.format(Calendar.getInstance().getTime());
        if (sales != null) {
            Cart cart = new Cart(sales.getId(), sales.getImage(), sales.getPrice(), quantity, date, sales.getName());
            Cart data1 = dbHelper.getCart(sales.getId());
            String id_ = data1.getId();
            if (id_ == null) {
                dbHelper.insertCart(cart);
                AppErrorsManager.showSuccessDialog(this, getString(R.string.add_to_your_cart_successfully),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                countTxt_.setVisibility(View.VISIBLE);
                                countTxt_.setText(String.valueOf(dbHelper.getCartCount()));
                            }
                        });
            } else {
                dbHelper.updateCart(cart);
                AppErrorsManager.showSuccessDialog(this, getString(R.string.update_product_successfully),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                countTxt_.setVisibility(View.VISIBLE);
                                countTxt_.setText(String.valueOf(dbHelper.getCartCount()));
                            }
                        });
            }
        } else {
            Cart cart = new Cart(offer.getId(), offer.getImage(), offer.getPrice(), quantity, date, offer.getName());
            Cart data1 = dbHelper.getCart(offer.getId());
            String id_ = data1.getId();
            if (id_ == null) {
                dbHelper.insertCart(cart);
                AppErrorsManager.showSuccessDialog(this, getString(R.string.add_to_your_cart_successfully),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                countTxt_.setVisibility(View.VISIBLE);
                                countTxt_.setText(String.valueOf(dbHelper.getCartCount()));
                            }
                        });
            } else {
                dbHelper.updateCart(cart);
                AppErrorsManager.showSuccessDialog(this, getString(R.string.update_product_successfully),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                countTxt_.setVisibility(View.VISIBLE);
                                countTxt_.setText(String.valueOf(dbHelper.getCartCount()));
                            }
                        });
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (dbHelper.getCartCount() == 0) {
//            countTxt_.setVisibility(View.GONE);
//        } else {
//            countTxt_.setVisibility(View.VISIBLE);
//            countTxt_.setText(String.valueOf(dbHelper.getCartCount()));
//        }
//    }
}
