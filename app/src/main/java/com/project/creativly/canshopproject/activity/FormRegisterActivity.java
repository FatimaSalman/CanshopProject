package com.project.creativly.canshopproject.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.fragment.SignInFragment;
import com.project.creativly.canshopproject.fragment.SignUpFragment;
import com.project.creativly.canshopproject.manager.AppPreferences;

public class FormRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private View view1, view2;
    private TextView signUpTxt, signInTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_register);
        init();
        if (!AppPreferences.getString(this, "token").equals("0")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    public void init() {
        view2 = findViewById(R.id.view2);
        view1 = findViewById(R.id.view1);
        signUpTxt = findViewById(R.id.signUpTxt);
        signInTxt = findViewById(R.id.signInTxt);
        RelativeLayout signUpLayout = findViewById(R.id.signUpLayout);
        RelativeLayout signInLayout = findViewById(R.id.SignInLayout);
        signUpLayout.setOnClickListener(this);
        signInLayout.setOnClickListener(this);
        showOtherFragment();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.signUpLayout) {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            signUpTxt.setTextColor(getResources().getColor(R.color.colorHead));
            signInTxt.setTextColor(getResources().getColor(R.color.colorGrayText));
            replaceFragment(new SignUpFragment());
        } else if (id == R.id.SignInLayout) {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            signInTxt.setTextColor(getResources().getColor(R.color.colorHead));
            signUpTxt.setTextColor(getResources().getColor(R.color.colorGrayText));
            replaceFragment(new SignInFragment());
        }
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
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);
        signUpTxt.setTextColor(getResources().getColor(R.color.colorHead));
        signInTxt.setTextColor(getResources().getColor(R.color.colorGrayText));
        replaceFragment(new SignUpFragment());
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
