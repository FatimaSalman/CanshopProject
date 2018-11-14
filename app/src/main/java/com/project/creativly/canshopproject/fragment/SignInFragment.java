package com.project.creativly.canshopproject.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.project.creativly.canshopproject.activity.ForgetPasswordActivity;
import com.project.creativly.canshopproject.activity.MainActivity;
import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.callback.LoginCallback;
import com.project.creativly.canshopproject.callback.RegisterCallback;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.AppPreferences;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.manager.FontManager;
import com.project.creativly.canshopproject.model.User;

import java.util.Objects;


public class SignInFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout usernameWrapper, passwordWrapper;
    private EditText usernameEditText, passwordEditText;
    private ConnectionManager connectionManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        connectionManager = new ConnectionManager(getActivity());
        init(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void init(View view) {

        usernameWrapper = view.findViewById(R.id.usernameWrapper);
        passwordWrapper = view.findViewById(R.id.passwordWrapper);

        passwordEditText = view.findViewById(R.id.passwordEditText);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordWrapper.setTypeface(FontManager.getTypeface(Objects.requireNonNull(getActivity())));
        passwordEditText.setTypeface(FontManager.getTypeface(Objects.requireNonNull(getActivity())));
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = usernameEditText.getText().toString().trim();
                if (email.isEmpty()) {
                    usernameWrapper.setError(getResources().getString(R.string.this_filed_required));
                    usernameEditText.requestFocus();
                } else {
                    usernameWrapper.setErrorEnabled(false);
                }
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = passwordEditText.getText().toString().trim();
                if (password.isEmpty()) {
                    passwordWrapper.setError(getResources().getString(R.string.this_filed_required));
                    passwordEditText.requestFocus();
                } else if (password.length() < 6) {
                    passwordWrapper.setError(getResources().getString(R.string.password_should_be_6_characters));
                    passwordEditText.requestFocus();
                } else {
                    passwordWrapper.setErrorEnabled(false);
                }
            }
        });

        Button signInButton = view.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(this);
        RelativeLayout forgetLayout = view.findViewById(R.id.forgetLayout);
        forgetLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.signInButton) {
            loginForm();
        } else if (id == R.id.forgetLayout) {
            Intent intent = new Intent(getActivity(), ForgetPasswordActivity.class);
            startActivity(intent);
        }
    }

    private void loginForm() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            usernameWrapper.setError(getString(R.string.this_filed_required));
        } else if (TextUtils.isEmpty(password)) {
            passwordWrapper.setError(getString(R.string.this_filed_required));
        } else if (password.length() < 6) {
            passwordWrapper.setError(getString(R.string.password_should_be_6_characters));
        } else {
            User user = new User();
            user.setName(username);
            user.setPassword(password);
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
            connectionManager.login(user, new LoginCallback() {
                @Override
                public void onLoginDone(User user) {
                    progressDialog.dismiss();
                    AppPreferences.saveString(getActivity(), "token", user.getToken());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();
                    Log.e("errr", error);
                    AppErrorsManager.showErrorDialog(getActivity(), error);
                }
            });

        }
    }

}
