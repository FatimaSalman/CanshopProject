package com.project.creativly.canshopproject.activity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.manager.AppLanguage;

import java.util.Locale;

public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad = AppLanguage.getLanguage(this); // your language
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
        setContentView(R.layout.activity_privacy);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init() {
        RelativeLayout backLayout = findViewById(R.id.backLayout);
        Button approveBtn = findViewById(R.id.approveBtn);
        approveBtn.setVisibility(View.GONE);

        backLayout.setOnClickListener(this);
        approveBtn.setOnClickListener(this);

        final ProgressBar waitProgress = findViewById(R.id.waitProgress);
        waitProgress.setVisibility(View.VISIBLE);

        WebView mWebview = findViewById(R.id.help_webview);

        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (waitProgress.getVisibility() == View.VISIBLE) {
                    waitProgress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(PrivacyActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        mWebview.loadUrl("https://www.canshop.net/terms-and-conditions/");

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backLayout) {
            finish();
        }
    }
}
