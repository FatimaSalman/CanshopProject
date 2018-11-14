package com.project.creativly.canshopproject.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.fragment.MyFavoriteFragment;
import com.project.creativly.canshopproject.fragment.MyOrderFragment;
import com.project.creativly.canshopproject.manager.AppPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView orderTxt;
    private TextView favoriteTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        ConnectionManager connectionManager = new ConnectionManager(this);
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(getString(R.string.loading));
        init();
        showOtherFragment();
    }

    public void init() {
        orderTxt = findViewById(R.id.orderTxt);
        TextView dateTxt = findViewById(R.id.dateTxt);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String date = format.format(Calendar.getInstance().getTime());
        dateTxt.setText(date);
        TextView userName = findViewById(R.id.userName);
        userName.setText(AppPreferences.getString(this, "username"));
        orderTxt.setOnClickListener(this);
        favoriteTxt = findViewById(R.id.favoriteTxt);
        favoriteTxt.setOnClickListener(this);

        RelativeLayout settingLayout = findViewById(R.id.settingLayout);
        settingLayout.setOnClickListener(this);
        RelativeLayout backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentReplace, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }

    public void showOtherFragment() {
        replaceFragment(new MyOrderFragment());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.orderTxt) {
            replaceFragment(new MyOrderFragment());
            orderTxt.setTextColor(getResources().getColor(R.color.colorWhite));
            favoriteTxt.setTextColor(getResources().getColor(R.color.colorWhiteOpacity));
        } else if (id == R.id.favoriteTxt) {
            replaceFragment(new MyFavoriteFragment());
            orderTxt.setTextColor(getResources().getColor(R.color.colorWhiteOpacity));
            favoriteTxt.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if (id == R.id.settingLayout) {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.backLayout) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
}
