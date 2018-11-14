package com.project.creativly.canshopproject.activity;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.manager.AppLanguage;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init() {
        WebView mWebView = findViewById(R.id.webView);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(ForgetPasswordActivity.this, getString(R.string.error_) + description, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
        if (AppLanguage.getLanguage(ForgetPasswordActivity.this).equals("ar")) {
            mWebView.loadUrl("https://www.canshop.net/ar/%D8%AD%D8%B3%D8%A7%D8%A8%D9%8A/lost-password/");
        } else
            mWebView.loadUrl(" https://www.canshop.net/my-account/lost-password/");

    }
}
