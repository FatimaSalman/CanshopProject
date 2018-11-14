package com.project.creativly.canshopproject.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.callback.InstallCallback;
import com.project.creativly.canshopproject.callback.LoginCallback;
import com.project.creativly.canshopproject.manager.AppErrorsManager;
import com.project.creativly.canshopproject.manager.ConnectionManager;
import com.project.creativly.canshopproject.manager.FontManager;
import com.project.creativly.canshopproject.model.OrderDetails;
import com.project.creativly.canshopproject.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout firstNameWrapper;
    private TextInputLayout lastNameWrapper;
    private TextInputLayout phoneWrapper;
    private TextInputLayout addressFirstWrapper;
    private TextInputLayout cityWrapper;
    private EditText firstNameEditText, lastNameEditText, phoneEditText, addressFirstEditText,
            cityEditText, countryEditText, emailEditText;
    //    companyEditText, addressSecondEditText, stateEditText,postCodeEditText
    private ConnectionManager connectionManager;
    private ProgressDialog progressDialog;
    private String email;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_address);
        connectionManager = new ConnectionManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        init();
        getProfile();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void init() {
        firstNameWrapper = findViewById(R.id.firstNameWrapper);
        lastNameWrapper = findViewById(R.id.lastNameWrapper);
//        TextInputLayout companyWrapper = findViewById(R.id.companyWrapper);
//        TextInputLayout emailWrapper = findViewById(R.id.emailWrapper);
        phoneWrapper = findViewById(R.id.phoneWrapper);
        addressFirstWrapper = findViewById(R.id.addressFirstWrapper);
//        TextInputLayout addressSecondWrapper = findViewById(R.id.addressSecondWrapper);
        cityWrapper = findViewById(R.id.cityWrapper);
//        TextInputLayout stateWrapper = findViewById(R.id.stateWrapper);
//        TextInputLayout postCodeWrapper = findViewById(R.id.postCodeWrapper);
//        TextInputLayout countryWrapper = findViewById(R.id.countryWrapper);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
//        companyEditText = findViewById(R.id.companyEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressFirstEditText = findViewById(R.id.addressFirstEditText);
//        addressSecondEditText = findViewById(R.id.addressSecondEditText);
        cityEditText = findViewById(R.id.cityEditText);
//        stateEditText = findViewById(R.id.stateEditText);
//        postCodeEditText = findViewById(R.id.postCodeEditText);
        countryEditText = findViewById(R.id.countryEditText);

        Button nextButton = findViewById(R.id.nextButton);
        RelativeLayout backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        nextButton.setOnClickListener(this);

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
//                String firstName = firstNameEditText.getText().toString().trim();
//                if (firstName.isEmpty()) {
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
//                String lastName = lastNameEditText.getText().toString().trim();
//                if (lastName.isEmpty()) {
//                    lastNameWrapper.setError(getResources().getString(R.string.this_filed_required));
//                    lastNameEditText.requestFocus();
//                } else {
//                    lastNameWrapper.setErrorEnabled(false);
//                }
//            }
//        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = phoneEditText.getText().toString().trim();
                if (phone.isEmpty()) {
                    phoneEditText.setError(getResources().getString(R.string.this_filed_required));
                    phoneWrapper.requestFocus();
                } else if (FontManager.isValidPhone(phone)) {
                    phoneWrapper.setError(getResources().getString(R.string.this_filed_required));
                    phoneEditText.requestFocus();
                } else {
                    phoneWrapper.setErrorEnabled(false);
                }
            }
        });
        addressFirstEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String address = addressFirstEditText.getText().toString().trim();
                if (address.isEmpty()) {
                    addressFirstWrapper.setError(getResources().getString(R.string.this_filed_required));
                    addressFirstEditText.requestFocus();
                } else {
                    addressFirstWrapper.setErrorEnabled(false);
                }
            }
        });
        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String city = cityEditText.getText().toString().trim();
                if (city.isEmpty()) {
                    cityWrapper.setError(getResources().getString(R.string.this_filed_required));
                    cityEditText.requestFocus();
                } else {
                    cityWrapper.setErrorEnabled(false);
                }
            }
        });
    }

    public void getProfile() {
        connectionManager.showProfile(new LoginCallback() {
            @Override
            public void onLoginDone(User user) {
                progressDialog.dismiss();
                firstNameEditText.setText(user.getFirst_name());
                lastNameEditText.setText(user.getLast_name());
                email = user.getEmail();
                emailEditText.setText(user.getEmail());
                emailEditText.setEnabled(false);
                countryEditText.setText(R.string.kuwait);
                countryEditText.setEnabled(false);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                AppErrorsManager.showErrorDialog(AddressActivity.this, error);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.backLayout) {
            finish();
        } else if (id == R.id.nextButton) {
            placeOrder();
        }
    }

    public void placeOrder() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
//        String company = companyEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address_1 = addressFirstEditText.getText().toString().trim();
//        String address_2 = addressSecondEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
//        String state = stateEditText.getText().toString().trim();
//        String postcode = postCodeEditText.getText().toString().trim();
        String country = "KW";
        String payment_m = "cod";

        if (TextUtils.isEmpty(firstName)) {
            firstNameWrapper.setError(getString(R.string.this_filed_required));
            firstNameEditText.requestFocus();
        } else if (TextUtils.isEmpty(lastName)) {
            lastNameWrapper.setError(getString(R.string.this_filed_required));
            lastNameEditText.requestFocus();
        } else if (TextUtils.isEmpty(phone)) {
            phoneWrapper.setError(getString(R.string.this_filed_required));
            phoneEditText.requestFocus();
        } else if (TextUtils.isEmpty(address_1)) {
            addressFirstWrapper.setError(getString(R.string.this_filed_required));
            addressFirstEditText.requestFocus();
        } else if (TextUtils.isEmpty(city)) {
            cityWrapper.setError(getString(R.string.this_filed_required));
            cityEditText.requestFocus();
        } else {
            final OrderDetails orderDetails = new OrderDetails();
            orderDetails.setFirstName(firstName);
            orderDetails.setLastName(lastName);
            orderDetails.setCompany("");
            orderDetails.setEmail(email);
            orderDetails.setPhone(phone);
            orderDetails.setAddress_1(address_1);
            orderDetails.setAddress_2("");
            orderDetails.setCity(city);
            orderDetails.setState("");
            orderDetails.setPostcode("");
            orderDetails.setCountry(country);
            orderDetails.setPayment_m(payment_m);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String date = format.format(Calendar.getInstance().getTime());
            orderDetails.setCreated_at(date);
            progressDialog.show();
            connectionManager.placeOrder(orderDetails, new InstallCallback() {
                @Override
                public void onStatusDone(String status) {
                    progressDialog.dismiss();
                    AppErrorsManager.showSuccessDialog(AddressActivity.this, status, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            DBHelper dbHelper = new DBHelper(AddressActivity.this);
//                            for (Cart cart : dbHelper.getAllCart()) {
//                                dbHelper.deleteCart(cart.getId());
//                            }
                            Intent intent = new Intent(AddressActivity.this, DoneActivity.class);
                            intent.putExtra("orderDetails", orderDetails);
                            startActivity(intent);
                            finish();
                        }
                    });

                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();
                    AppErrorsManager.showErrorDialog(AddressActivity.this, error);
                }
            });
        }
    }
}
