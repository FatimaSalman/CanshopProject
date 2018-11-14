package com.project.creativly.canshopproject.fragment;


import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.support.design.widget.TextInputLayout;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.activity.MainActivity;
import com.project.creativly.canshopproject.activity.PrivacyActivity;
import com.project.creativly.canshopproject.callback.RegisterCallback;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.AppPreferences;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.manager.FontManager;
import com.project.creativly.canshopproject.model.User;

import java.util.Objects;

public class SignUpFragment extends Fragment implements View.OnClickListener {
    private TextInputLayout usernameWrapper, emailWrapper, passwordWrapper, confirmPassWrapper;
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPassEditText;
    private ConnectionManager connectionManager;
    private CheckBox checkboxTerm;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        connectionManager = new ConnectionManager(getActivity());
        init(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void init(View view) {

        usernameWrapper = view.findViewById(R.id.usernameWrapper);
        emailWrapper = view.findViewById(R.id.emailWrapper);
        passwordWrapper = view.findViewById(R.id.passwordWrapper);
        confirmPassWrapper = view.findViewById(R.id.confirmPassWrapper);

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        passwordWrapper.setTypeface(FontManager.getTypeface(Objects.requireNonNull(getActivity())));
        passwordEditText.setTypeface(FontManager.getTypeface(Objects.requireNonNull(getActivity())));
        confirmPassWrapper.setTypeface(FontManager.getTypeface(Objects.requireNonNull(getActivity())));
        confirmPassEditText = view.findViewById(R.id.confirmPassEditText);
        confirmPassEditText.setTypeface(FontManager.getTypeface(getActivity()));

        emailEditText = view.findViewById(R.id.emailEditText);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = emailEditText.getText().toString().trim();
                if (email.isEmpty()) {
                    emailWrapper.setError(getResources().getString(R.string.this_filed_required));
                    emailEditText.requestFocus();
                } else if (FontManager.isValidEmail(email)) {
                    emailWrapper.setError(getResources().getString(R.string.this_filed_required));
                    emailEditText.requestFocus();
                } else {
                    emailWrapper.setErrorEnabled(false);
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
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String userName = usernameEditText.getText().toString().trim();
                if (userName.isEmpty()) {
                    usernameWrapper.setError(getResources().getString(R.string.this_filed_required));
                    usernameEditText.requestFocus();
                } else {
                    usernameWrapper.setErrorEnabled(false);
                }
            }
        });
        confirmPassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPassEditText.getText().toString().trim();
                if (confirmPassword.isEmpty()) {
                    confirmPassWrapper.setError(getResources().getString(R.string.this_filed_required));
                    confirmPassEditText.requestFocus();
                } else if (!TextUtils.equals(confirmPassword, password)) {
                    confirmPassWrapper.setError(getResources().getString(R.string.this_filed_required));
                    confirmPassEditText.requestFocus();
                } else {
                    confirmPassWrapper.setErrorEnabled(false);
                }
            }
        });

        Button registerBtn = view.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
        checkboxTerm = view.findViewById(R.id.checkboxTerm);
        checkboxTerm.setTypeface(FontManager.getTypeface(Objects.requireNonNull(getActivity())));
        checkboxTerm.setOnClickListener(this);

        checkboxTerm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxTerm.setError(null);
                } else {
                    checkboxTerm.setError(getResources().getString(R.string.agree_of_terms));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.registerBtn) {
            registerForm();
        } else if (id == R.id.checkboxTerm) {
            Intent intent = new Intent(getActivity(), PrivacyActivity.class);
            startActivity(intent);
        }
    }

    private void registerForm() {
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String userName = usernameEditText.getText().toString().trim();
        String confirmPassword = confirmPassEditText.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            usernameWrapper.setError(getString(R.string.this_filed_required));
        } else if (TextUtils.isEmpty(email)) {
            emailWrapper.setError(getString(R.string.this_filed_required));
        } else if (FontManager.isValidEmail(email)) {
            emailWrapper.setError(getString(R.string.this_filed_required));
        } else if (TextUtils.isEmpty(password)) {
            passwordWrapper.setError(getString(R.string.this_filed_required));
        } else if (password.length() < 6) {
            passwordWrapper.setError(getString(R.string.password_should_be_6_characters));
        } else if (confirmPassword.isEmpty()) {
            confirmPassWrapper.setError(getResources().getString(R.string.this_filed_required));
            confirmPassEditText.requestFocus();
        } else if (!TextUtils.equals(confirmPassword, password)) {
            confirmPassWrapper.setError(getResources().getString(R.string.this_filed_required));
            confirmPassEditText.requestFocus();
        } else if (!checkboxTerm.isChecked()) {
            checkboxTerm.setError(getResources().getString(R.string.agree_of_terms));
        } else {
            User user = new User();
            user.setName(userName);
            user.setEmail(email);
            user.setPassword(password);
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
            connectionManager.register(user, new RegisterCallback() {
                @Override
                public void onUserRegisterDone(User user) {
                    progressDialog.dismiss();
                    AppPreferences.saveString(getActivity(), "token", user.getToken());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();
                    Log.e("errr", error);
                    if (TextUtils.equals(error, "username already exists")) {
                        usernameWrapper.setError(error);
                        usernameEditText.requestFocus();
                    } else if (TextUtils.equals(error, "Email you are trying to register is exists")) {
                        emailWrapper.setError(error);
                        emailEditText.requestFocus();
                    } else {
                        AppErrorsManager.showErrorDialog(getActivity(), error);
                    }
                }
            });

        }
    }


}
