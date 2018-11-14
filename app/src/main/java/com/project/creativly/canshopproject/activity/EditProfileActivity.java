package com.project.creativly.canshopproject.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.callback.InstallCallback;
import com.project.creativly.canshopproject.callback.LoginCallback;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.manager.FontManager;
import com.project.creativly.canshopproject.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout passwordWrapper, emailWrapper, lastNameWrapper, firstNameWrapper;
    private EditText passwordEditText, usernameEditText, emailEditText, firstNameEditText, lastNameEditText;
    private ConnectionManager connectionManager;
    private ProgressDialog progressDialog;
    private ScrollView scrollView;
    private TextView userNameTxt, dateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        connectionManager = new ConnectionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        init();
        getProfile();
    }

    public void init() {
        RelativeLayout backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);

        scrollView = findViewById(R.id.scrollView);
        scrollView.setVisibility(View.GONE);
        userNameTxt = findViewById(R.id.userNameTxt);
        dateTxt = findViewById(R.id.dateTxt);
        passwordWrapper = findViewById(R.id.passwordWrapper);
        emailWrapper = findViewById(R.id.emailWrapper);
        lastNameWrapper = findViewById(R.id.lastNameWrapper);
        firstNameWrapper = findViewById(R.id.firstNameWrapper);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        passwordWrapper.setTypeface(FontManager.getTypeface(this));
        passwordEditText.setTypeface(FontManager.getTypeface(this));
        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(this);


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
//        firstNameEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String userName = firstNameEditText.getText().toString().trim();
//                if (userName.isEmpty()) {
//                    firstNameWrapper.setError(getResources().getString(R.string.this_filed_required));
//                    firstNameEditText.requestFocus();
//                } else {
//                    firstNameWrapper.setErrorEnabled(false);
//                }
//            }
//        });
//        lastNameEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String userName = lastNameEditText.getText().toString().trim();
//                if (userName.isEmpty()) {
//                    lastNameWrapper.setError(getResources().getString(R.string.this_filed_required));
//                    lastNameEditText.requestFocus();
//                } else {
//                    lastNameWrapper.setErrorEnabled(false);
//                }
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backLayout) {
            finish();
        } else if (id == R.id.editButton) {
            editProfile();
        }
    }

    public void getProfile() {
        connectionManager.showProfile(new LoginCallback() {
            @Override
            public void onLoginDone(User user) {
                progressDialog.dismiss();
                scrollView.setVisibility(View.VISIBLE);
                userNameTxt.setText(user.getName());
                usernameEditText.setText(user.getName());
                firstNameEditText.setText(user.getFirst_name());
                lastNameEditText.setText(user.getLast_name());
                usernameEditText.setEnabled(false);
                emailEditText.setText(user.getEmail());
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
                String date = format.format(Calendar.getInstance().getTime());
                dateTxt.setText(date);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                AppErrorsManager.showErrorDialog(EditProfileActivity.this, error);
            }
        });
    }

    public void editProfile() {
        progressDialog.show();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        User user = new User();
        user.setFirst_name(firstName);
        user.setLast_name(lastName);
        user.setEmail(email);
        user.setPassword(password);
        connectionManager.editProfile(user, new InstallCallback() {
            @Override
            public void onStatusDone(String status) {
                progressDialog.dismiss();
                AppErrorsManager.showSuccessDialog(EditProfileActivity.this, status, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!TextUtils.isEmpty(password)) {
                            FontManager.logOut(EditProfileActivity.this);
                        }
                    }
                });
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                AppErrorsManager.showSuccessDialog(EditProfileActivity.this, error);

            }
        });
    }
}
