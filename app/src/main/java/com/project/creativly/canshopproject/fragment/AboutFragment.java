package com.project.creativly.canshopproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.activity.MainActivity;
import com.project.creativly.canshopproject.manager.AppLanguage;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        init(view);
        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init(View view) {
        WebView mWebView = view.findViewById(R.id.webView);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
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
                Toast.makeText(getActivity(), "Error:" + description, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
        if (AppLanguage.getLanguage(getActivity()).equals("ar")) {
            mWebView.loadUrl("https://www.canshop.net/ar/%D9%85%D9%86-%D9%86%D8%AD%D9%86/");
        } else
            mWebView.loadUrl("https://www.canshop.net/about-us/");

    }
}
