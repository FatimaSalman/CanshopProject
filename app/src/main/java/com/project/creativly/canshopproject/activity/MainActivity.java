package com.project.creativly.canshopproject.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.callback.CategoriesCallback;
import com.project.creativly.canshopproject.callback.LoginCallback;
import com.project.creativly.canshopproject.database.DBHelper;
import com.project.creativly.canshopproject.fragment.AboutFragment;
import com.project.creativly.canshopproject.fragment.CategoriesFragment;
import com.project.creativly.canshopproject.fragment.ContactFragment;
import com.project.creativly.canshopproject.fragment.HomeFragment;
import com.project.creativly.canshopproject.fragment.TermsFragment;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.AppLanguage;
import com.project.creativly.canshopproject.manager.AppPreferences;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.manager.FontManager;
import com.project.creativly.canshopproject.model.Category;
import com.project.creativly.canshopproject.model.User;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout menuItemLayout;
    private TextView languageTxt, countTxt;
    private ConnectionManager connectionManager;
    private String languageToLoad;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageToLoad = AppLanguage.getLanguage(this); // your language
        Locale locale;
        Log.e("ddd", languageToLoad);

        switch (languageToLoad) {
            case "العربية":
                locale = new Locale("ar");
                break;
            case "English":
                locale = new Locale("en");
                break;
            case "ar":
                locale = new Locale("ar");
                break;
            case "en":
                locale = new Locale("en");
                break;
            default:
                locale = new Locale(languageToLoad);
                break;
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
        connectionManager = new ConnectionManager(this);
        dbHelper = new DBHelper(this);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        init();
        getProfile();
        showOtherFragment();
    }

    public void init() {
        menuItemLayout = findViewById(R.id.menuItemLayout);
        countTxt = findViewById(R.id.countTxt);
        languageTxt = findViewById(R.id.languageTxt);
        if (languageToLoad.equals("ar")) {
            languageTxt.setText(getString(R.string.arabic));
        } else {
            languageTxt.setText(getString(R.string.english));
        }
        String count = getIntent().getStringExtra("count");
        if (count != null) {
            if (dbHelper.getCartCount() == 0) {
                countTxt.setVisibility(View.GONE);
            } else {
                countTxt.setVisibility(View.VISIBLE);
                countTxt.setText(String.valueOf(dbHelper.getCartCount()));
            }
        } else {
            if (dbHelper.getCartCount() == 0) {
                countTxt.setVisibility(View.GONE);
            } else {
                countTxt.setVisibility(View.VISIBLE);
                countTxt.setText(String.valueOf(dbHelper.getCartCount()));
            }
        }
        CircularImageView profileImg = findViewById(R.id.profileImg);
        RelativeLayout menuLayout = findViewById(R.id.menuLayout);
        RelativeLayout cartLayout = findViewById(R.id.cartLayout);
        RelativeLayout languageLayout = findViewById(R.id.languageLayout);
        ImageView ic_close = findViewById(R.id.ic_close);
        TextView homeItem = findViewById(R.id.homeItem);
        TextView categoriesItem = findViewById(R.id.categoriesItem);
        TextView aboutItem = findViewById(R.id.aboutItem);
        TextView termsItem = findViewById(R.id.termsItem);
        TextView contactItem = findViewById(R.id.contactItem);
        TextView logOutItem = findViewById(R.id.logOutItem);
        homeItem.setOnClickListener(this);
        categoriesItem.setOnClickListener(this);
        aboutItem.setOnClickListener(this);
        termsItem.setOnClickListener(this);
        contactItem.setOnClickListener(this);
        ic_close.setOnClickListener(this);
        menuLayout.setOnClickListener(this);
        profileImg.setOnClickListener(this);
        cartLayout.setOnClickListener(this);
        logOutItem.setOnClickListener(this);
        languageLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.menuLayout) {
            menuItemLayout.setVisibility(View.VISIBLE);
        } else if (id == R.id.ic_close) {
            menuItemLayout.setVisibility(View.GONE);
        } else if (id == R.id.homeItem) {
            menuItemLayout.setVisibility(View.GONE);
            replaceFragment(new HomeFragment());
        } else if (id == R.id.categoriesItem) {
            menuItemLayout.setVisibility(View.GONE);
            replaceFragment(new CategoriesFragment());
        } else if (id == R.id.aboutItem) {
            menuItemLayout.setVisibility(View.GONE);
            replaceFragment(new AboutFragment());
        } else if (id == R.id.termsItem) {
            menuItemLayout.setVisibility(View.GONE);
            replaceFragment(new TermsFragment());
        } else if (id == R.id.contactItem) {
            menuItemLayout.setVisibility(View.GONE);
            replaceFragment(new ContactFragment());
        } else if (id == R.id.profileImg) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.cartLayout) {
            if (dbHelper.getCartCount() == 0) {
                AppErrorsManager.showSuccessDialog(this, getString(R.string.your_cart_not_has_any_product));
            } else {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.logOutItem) {
            FontManager.logOut(this);
        } else if (id == R.id.languageLayout) {
            openDialogLanguage();
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
        replaceFragment(new HomeFragment());
    }

    public void openDialogLanguage() {
        LayoutInflater factory = LayoutInflater.from(this);
        @SuppressLint("InflateParams") final View dialogView = factory.inflate(R.layout.select_language_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(dialogView);
        dialogView.findViewById(R.id.arabicBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLang("ar");
                AppLanguage.saveLanguage(MainActivity.this, "ar");
                languageTxt.setText(getString(R.string.arabic));
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
                deleteDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.englishBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLang("en");
                AppLanguage.saveLanguage(MainActivity.this, "en");
                languageTxt.setText(getString(R.string.english));
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        this.getBaseContext().getResources().updateConfiguration(config,
                this.getBaseContext().getResources().getDisplayMetrics());
//        updateTexts();
    }

    public void getProfile() {
//        progressDialog.show();
        connectionManager.showProfile(new LoginCallback() {
            @Override
            public void onLoginDone(User user) {
//                progressDialog.dismiss();
                AppPreferences.saveString(MainActivity.this, "username", user.getName());
                AppPreferences.saveString(MainActivity.this, "first_name", user.getFirst_name());
                AppPreferences.saveString(MainActivity.this, "last_name", user.getLast_name());
                AppPreferences.saveString(MainActivity.this, "email", user.getEmail());
            }

            @Override
            public void onError(String error) {
//                progressDialog.dismiss();
                AppErrorsManager.showErrorDialog(MainActivity.this, error);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHelper.getCartCount() == 0) {
            countTxt.setVisibility(View.GONE);
        } else {
            countTxt.setVisibility(View.VISIBLE);
            countTxt.setText(String.valueOf(dbHelper.getCartCount()));
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
