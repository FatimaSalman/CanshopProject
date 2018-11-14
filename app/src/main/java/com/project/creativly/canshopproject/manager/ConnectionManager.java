package com.project.creativly.canshopproject.manager;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.project.creativly.canshopproject.R;
import com.project.creativly.canshopproject.callback.CategoriesCallback;
import com.project.creativly.canshopproject.callback.ConnectionManagerInterface;
import com.project.creativly.canshopproject.callback.InstallCallback;
import com.project.creativly.canshopproject.callback.InternetAvailableCallback;
import com.project.creativly.canshopproject.callback.LoginCallback;
import com.project.creativly.canshopproject.callback.OfferCallback;
import com.project.creativly.canshopproject.callback.OrderCallback;
import com.project.creativly.canshopproject.callback.OrderDetailsCallback;
import com.project.creativly.canshopproject.callback.ProductCallback;
import com.project.creativly.canshopproject.callback.RegisterCallback;
import com.project.creativly.canshopproject.database.DBHelper;
import com.project.creativly.canshopproject.model.Cart;
import com.project.creativly.canshopproject.model.Category;
import com.project.creativly.canshopproject.model.Offer;
import com.project.creativly.canshopproject.model.Order;
import com.project.creativly.canshopproject.model.OrderDetails;
import com.project.creativly.canshopproject.model.Sales;
import com.project.creativly.canshopproject.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class ConnectionManager implements ConnectionManagerInterface {

//    private static int LOAD_LIMIT = 10;

    private Context mContext;
    private ExecutorService mExecutorService;
    private Handler handler;

    public ConnectionManager(Context context) {
        this.mContext = context;
        mExecutorService = Executors.newFixedThreadPool(2);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void login(final User user, final LoginCallback callback) {
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void run() {
                                    MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                            .addFormDataPart("username", user.getName())
                                            .addFormDataPart("password", user.getPassword());
                                    RequestBody requestBody = formBody.build();
                                    final OkHttpClient client = new OkHttpClient();
                                    okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                            + "auth/login").post(requestBody).build();
                                    final okhttp3.Response response;
                                    try {
                                        response = client.newCall(request).execute();
                                        assert response.body() != null;
                                        String response_data = Objects.requireNonNull(response.body()).string();
                                        Log.e("aaa", response_data);
                                        if (response_data != null) {
                                            try {
                                                final JSONObject jsonObject = new JSONObject(response_data);
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            String status = jsonObject.getString("status");
                                                            if (TextUtils.equals(status, "true")) {
                                                                String token = jsonObject.getString("token");
                                                                User user1 = new User();
                                                                user1.setToken(token);
                                                                callback.onLoginDone(user1);
                                                            } else if (TextUtils.equals(status, "false")) {
                                                                if (AppLanguage.getLanguage(mContext).equals("ar")) {
                                                                    String message_ar = jsonObject.getString("message_ar");
                                                                    callback.onError(message_ar);
                                                                } else {
                                                                    String message_en = jsonObject.getString("message_en");
                                                                    callback.onError(message_en);
                                                                }
                                                            }
                                                        } catch (final JSONException e) {
                                                            e.printStackTrace();
                                                            handler.post(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    callback.onError(mContext.getString(R.string.something_wrong));
                                                                }
                                                            });
                                                        }
                                                    }
                                                });

                                            } catch (final Exception e) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        assert callback != null;
                                                        callback.onError(mContext.getString(R.string.something_wrong));

                                                    }
                                                });
                                            }
                                        } else {
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    callback.onError(mContext.getString(R.string.no_internet_connection));
                                                }
                                            });

                                        }
                                    } catch (final IOException e) {
                                        e.printStackTrace();
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                callback.onError(mContext.getString(R.string.something_wrong));
                                            }
                                        });
                                    }
                                }
                            }).start();

                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void register(final User user, final RegisterCallback callback) {
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("username", user.getName())
                                    .addFormDataPart("password", user.getPassword())
                                    .addFormDataPart("email", user.getEmail());
                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "auth/register").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("aaa", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    String status = jsonObject.getString("status");
                                                    if (TextUtils.equals(status, "true")) {
                                                        String token = jsonObject.getString("token");
                                                        User user1 = new User();
                                                        user1.setToken(token);
                                                        callback.onUserRegisterDone(user1);
                                                    } else if (TextUtils.equals(status, "false")) {
                                                        if (AppLanguage.getLanguage(mContext).equals("ar")) {
                                                            String message_ar = jsonObject.getString("message_ar");
                                                            callback.onError(message_ar);
                                                        } else {
                                                            String message_en = jsonObject.getString("message_en");
                                                            callback.onError(message_en);
                                                        }
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void productOffers(final int per_page, final int page, final OfferCallback callback) {
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("per_page", String.valueOf(per_page))
                                    .addFormDataPart("page", String.valueOf(page))
                                    .addFormDataPart("langu", AppLanguage.getLanguage(mContext));
                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "products/offers").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("aaa", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (jsonObject.has("results_ar")) {
                                                        String results = jsonObject.getString("results_ar");
                                                        JSONArray jsonArray = new JSONArray(results);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            String lang = jsonObject1.getString("lang");
                                                            String id = jsonObject1.getString("id");
                                                            String name = jsonObject1.getString("name");
                                                            String price = jsonObject1.getString("price");
                                                            String description = jsonObject1.getString("description");
                                                            String image = jsonObject1.getString("image");
                                                            String stock_status = jsonObject1.getString("stock_status");
                                                            String imageUrl;
                                                            if (TextUtils.equals(image, "false")) {
                                                                imageUrl = "false";
                                                            } else {
                                                                JSONArray imageArray = new JSONArray(image);
                                                                imageUrl = imageArray.getString(0);
                                                            }
                                                            String date_created = jsonObject1.getString("date_created");
                                                            if (!TextUtils.equals(date_created, "null")) {
                                                                JSONObject dateJson = new JSONObject(date_created);
                                                                String date = dateJson.getString("date");
                                                                Offer offer = new Offer(lang, id, name, price, imageUrl,
                                                                        date, description, stock_status);
                                                                callback.onOfferDone(offer);
                                                            }
                                                        }
                                                    } else if (jsonObject.has("results_en")) {
                                                        String results = jsonObject.getString("results_en");
                                                        JSONArray jsonArray = new JSONArray(results);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            String lang = jsonObject1.getString("lang");
                                                            String id = jsonObject1.getString("id");
                                                            String name = jsonObject1.getString("name");
                                                            String description = jsonObject1.getString("description");
                                                            String price = jsonObject1.getString("price");
                                                            String image = jsonObject1.getString("image");
                                                            String stock_status = jsonObject1.getString("stock_status");
                                                            String imageUrl;
                                                            if (TextUtils.equals(image, "false")) {
                                                                imageUrl = "false";
                                                            } else {
                                                                JSONArray imageArray = new JSONArray(image);
                                                                imageUrl = imageArray.getString(0);
                                                            }
                                                            String date_created = jsonObject1.getString("date_created");
                                                            if (!TextUtils.equals(date_created, "null")) {
                                                                JSONObject dateJson = new JSONObject(date_created);
                                                                String date = dateJson.getString("date");
                                                                Offer offer = new Offer(lang, id, name, price,
                                                                        imageUrl, date, description, stock_status);
                                                                callback.onOfferDone(offer);
                                                            }
                                                        }
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void showProfile(final LoginCallback callback) {
        Log.e("token", AppPreferences.getString(mContext, "token"));
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("token", AppPreferences.getString(mContext, "token"));
                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "profile/show").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("profile/show", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    String status = jsonObject.getString("status");
                                                    if (TextUtils.equals(status, "true")) {
                                                        String user = jsonObject.getString("user");
                                                        JSONArray jsonArray = new JSONArray(user);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            String id = jsonObject1.getString("id");
                                                            String first_name = jsonObject1.getString("first_name");
                                                            String last_name = jsonObject1.getString("last_name");
                                                            String username = jsonObject1.getString("username");
                                                            String email = jsonObject1.getString("email");
                                                            User user1 = new User(id, first_name, last_name, username, email);
                                                            callback.onLoginDone(user1);
                                                        }
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void productLatest(final int per_page, final int page, final ProductCallback callback) {
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("per_page", String.valueOf(per_page))
                                    .addFormDataPart("page", String.valueOf(page))
                                    .addFormDataPart("langu", AppLanguage.getLanguage(mContext));
                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "products/latest").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("aaa", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (jsonObject.has("results_ar")) {
                                                        String results = jsonObject.getString("results_ar");
                                                        JSONArray jsonArray = new JSONArray(results);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            String lang = jsonObject1.getString("lang");
                                                            String id = jsonObject1.getString("id");
                                                            String name = jsonObject1.getString("name");
                                                            String price = jsonObject1.getString("price");
                                                            String image = jsonObject1.getString("image");
                                                            String description = jsonObject1.getString("description");
                                                            String stock_status = jsonObject1.getString("stock_status");
                                                            String imageUrl;
                                                            if (TextUtils.equals(image, "false")) {
                                                                imageUrl = "false";
                                                            } else {
                                                                JSONArray imageArray = new JSONArray(image);
                                                                imageUrl = imageArray.getString(0);
                                                            }
                                                            String date_created = jsonObject1.getString("date_created");
                                                            if (!TextUtils.equals(date_created, "null")) {
                                                                JSONObject dateJson = new JSONObject(date_created);
                                                                String date = dateJson.getString("date");
                                                                Sales sales = new Sales(id, imageUrl, price, name,
                                                                        lang, date, description, stock_status);
                                                                callback.onProductDone(sales);
                                                            }
                                                        }
                                                    } else if (jsonObject.has("results_en")) {
                                                        String results = jsonObject.getString("results_en");
                                                        JSONArray jsonArray = new JSONArray(results);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            String lang = jsonObject1.getString("lang");
                                                            String id = jsonObject1.getString("id");
                                                            String name = jsonObject1.getString("name");
                                                            String price = jsonObject1.getString("price");
                                                            String image = jsonObject1.getString("image");
                                                            String description = jsonObject1.getString("description");
                                                            String stock_status = jsonObject1.getString("stock_status");
                                                            String imageUrl;
                                                            if (TextUtils.equals(image, "false")) {
                                                                imageUrl = "false";
                                                            } else {
                                                                JSONArray imageArray = new JSONArray(image);
                                                                imageUrl = imageArray.getString(0);
                                                            }
                                                            String date_created = jsonObject1.getString("date_created");
                                                            if (!TextUtils.equals(date_created, "null")) {
                                                                JSONObject dateJson = new JSONObject(date_created);
                                                                String date = dateJson.getString("date");
                                                                Sales sales = new Sales(id, imageUrl, price,
                                                                        name, lang, date, description, stock_status);
                                                                callback.onProductDone(sales);
                                                            }
                                                        }
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void orderList(final OrderCallback callback) {
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("token", AppPreferences.getString(mContext, "token"));
                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "order/list").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("orderList", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    String results = jsonObject.getString("results");
                                                    if (TextUtils.equals(results, "[]")) {
                                                        callback.onOrderDone(new Order());
                                                    } else {
                                                        JSONArray jsonArray = new JSONArray(results);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            String order_id = jsonObject1.getString("order_id");
                                                            String order_date = jsonObject1.getString("order_date");
                                                            String type_ar = jsonObject1.getString("type_ar");
                                                            String type_en = jsonObject1.getString("type_en");
                                                            Order sales = new Order(order_id, order_date, type_ar, type_en);
                                                            callback.onOrderDone(sales);
                                                        }
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (
                                    Exception e)

                            {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else

                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void orderListShow(final String id, final OrderDetailsCallback callback) {
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("token", AppPreferences.getString(mContext, "token"))
                                    .addFormDataPart("id", id);
                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "order/show").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("orderList", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    String results = jsonObject.getString("results");
                                                    JSONObject resultJson = new JSONObject(results);
                                                    String date_created = resultJson.getString("date_created");
                                                    String total = resultJson.getString("total");
                                                    String billing = resultJson.getString("billing");
                                                    JSONObject billingJson = new JSONObject(billing);
                                                    String first_name = billingJson.getString("first_name");
                                                    String last_name = billingJson.getString("last_name");
                                                    String address_1 = billingJson.getString("address_1");
                                                    String address_2 = billingJson.getString("address_2");
                                                    String city = billingJson.getString("city");
                                                    String email = billingJson.getString("email");
                                                    String phone = billingJson.getString("phone");
                                                    String items = resultJson.getString("items");
                                                    JSONArray itemsArray = new JSONArray(items);
                                                    List<Cart> cartList = new ArrayList<>();
                                                    for (int i = 0; i < itemsArray.length(); i++) {
                                                        JSONObject object = itemsArray.getJSONObject(i);
                                                        String order_item_id = object.getString("order_item_id");
                                                        String order_item_name = object.getString("order_item_name");
                                                        String order_item_product_id = object.getString("order_item_product_id");
                                                        String order_item_quantity = object.getString("order_item_quantity");
                                                        String order_item_price = object.getString("order_item_price");
                                                        String order_item_total = object.getString("order_item_total");
                                                        Cart cart = new Cart(order_item_price + " " + mContext.getString(R.string.doller_sign), order_item_name,
                                                                order_item_quantity, order_item_id,
                                                                order_item_product_id, order_item_total, "");
                                                        cartList.add(cart);
                                                    }

                                                    OrderDetails sales = new OrderDetails(first_name, last_name, "", email,
                                                            phone, address_1, address_2, city, "",
                                                            "", "", "", date_created, cartList, total);
                                                    callback.onOrderDone(sales);

                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (
                                    Exception e)

                            {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else

                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void editProfile(final User user, final InstallCallback callback) {
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("token", AppPreferences.getString(mContext, "token"))
                                    .addFormDataPart("first_name", user.getFirst_name())
                                    .addFormDataPart("last_name", user.getLast_name())
                                    .addFormDataPart("password", user.getPassword())
                                    .addFormDataPart("email", user.getEmail());
                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "profile/edit").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("editProfile", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    String status = jsonObject.getString("status");
                                                    if (TextUtils.equals(status, "true")) {
                                                        if (AppLanguage.getLanguage(mContext).equals("ar")) {
                                                            String message_ar = jsonObject.getString("message_ar");
                                                            callback.onStatusDone(message_ar);
                                                        } else {
                                                            String message_en = jsonObject.getString("message_en");
                                                            callback.onStatusDone(message_en);
                                                        }
                                                    } else if (TextUtils.equals(status, "false")) {
                                                        if (AppLanguage.getLanguage(mContext).equals("ar")) {
                                                            String message_ar = jsonObject.getString("message_ar");
                                                            callback.onStatusDone(message_ar);
                                                        } else {
                                                            String message_en = jsonObject.getString("message_en");
                                                            callback.onStatusDone(message_en);
                                                        }
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void placeOrder(final OrderDetails orderDetails, final InstallCallback callback) {
        final DBHelper dbHelper = new DBHelper(mContext);
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("token", AppPreferences.getString(mContext, "token"))
                                    .addFormDataPart("first_name", orderDetails.getFirstName())
                                    .addFormDataPart("last_name", orderDetails.getLastName())
                                    .addFormDataPart("company", orderDetails.getCompany())
                                    .addFormDataPart("email", orderDetails.getEmail())
                                    .addFormDataPart("phone", orderDetails.getPhone())
                                    .addFormDataPart("address_1", orderDetails.getAddress_1())
                                    .addFormDataPart("address_2", orderDetails.getAddress_2())
                                    .addFormDataPart("city", orderDetails.getCity())
                                    .addFormDataPart("state", orderDetails.getState())
                                    .addFormDataPart("postcode", orderDetails.getPostcode())
                                    .addFormDataPart("country", orderDetails.getCountry())
                                    .addFormDataPart("payment_m", orderDetails.getPayment_m());
                            for (Cart cart : dbHelper.getAllCart()) {
                                formBody.addFormDataPart("product_id[0]" + "[" + cart.getId() + "]", cart.getQuantity());
                            }
                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "order/place").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("editProfile", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    String status = jsonObject.getString("status");
                                                    if (TextUtils.equals(status, "true")) {
                                                        if (AppLanguage.getLanguage(mContext).equals("ar")) {
                                                            String message_ar = jsonObject.getString("message_ar");
                                                            callback.onStatusDone(message_ar);
                                                        } else {
                                                            String message_en = jsonObject.getString("message_en");
                                                            callback.onStatusDone(message_en);
                                                        }
                                                    } else if (TextUtils.equals(status, "false")) {
                                                        if (AppLanguage.getLanguage(mContext).equals("ar")) {
                                                            String message_ar = jsonObject.getString("message_ar");
                                                            callback.onStatusDone(message_ar);
                                                        } else {
                                                            String message_en = jsonObject.getString("message_en");
                                                            callback.onStatusDone(message_en);
                                                        }
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void search(final String search, final ProductCallback callback) {
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("langu", AppLanguage.getLanguage(mContext))
                                    .addFormDataPart("search", search);

                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "search").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("search", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    String results = jsonObject.getString("results");
                                                    JSONArray jsonArray = new JSONArray(results);
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                                        String lang = jsonObject1.getString("lang");
                                                        String id = jsonObject1.getString("id");
                                                        String name = jsonObject1.getString("name");
                                                        String price = jsonObject1.getString("price");
                                                        String description = jsonObject1.getString("description");
                                                        String image = jsonObject1.getString("image");
                                                        String stock_status = jsonObject1.getString("stock_status");
                                                        String imageUrl;
                                                        if (TextUtils.equals(image, "false")) {
                                                            imageUrl = "false";
                                                        } else {
                                                            JSONArray imageArray = new JSONArray(image);
                                                            imageUrl = imageArray.getString(0);
                                                        }
                                                        String date_created = jsonObject1.getString("date_created");
                                                        if (!TextUtils.equals(date_created, "null")) {
                                                            JSONObject dateJson = new JSONObject(date_created);
                                                            String date = dateJson.getString("date");
                                                            Sales sales = new Sales(id, imageUrl, price, name, "", date, description, stock_status);
                                                            callback.onProductDone(sales);
                                                        }
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void getCategories(final CategoriesCallback callback) {
        Log.e("langu", AppLanguage.getLanguage(mContext));
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("langu", AppLanguage.getLanguage(mContext));

                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "tools/categories").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("categories", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (jsonObject.has("results_en")) {
                                                        Log.e("em", "en");
                                                        String results = jsonObject.getString("results_en");
                                                        JSONArray jsonArray = new JSONArray(results);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            String id = jsonObject1.getString("id");
                                                            String name = jsonObject1.getString("name");
                                                            String slug = jsonObject1.getString("slug");

                                                            Category category = new Category(id, slug, name);
                                                            callback.onCategories(category);
                                                        }
                                                    } else if (jsonObject.has("results_ar")) {
                                                        Log.e("em", "ar");
                                                        String results = jsonObject.getString("results_ar");
                                                        JSONArray jsonArray = new JSONArray(results);
                                                        for (int i = 0; i < jsonArray.length(); i++) {
                                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                            String id = jsonObject1.getString("id");
                                                            String name = jsonObject1.getString("name");
                                                            String slug = jsonObject1.getString("slug");

                                                            Category category = new Category(id, slug, name);
                                                            callback.onCategories(category);
                                                        }
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void productCategories(final int per_page, final int page, final String slug, final ProductCallback callback) {
        InternetConnectionUtils.isInternetAvailable(mContext.getApplicationContext(), new InternetAvailableCallback() {
            @Override
            public void onInternetAvailable(boolean isAvailable) {
                if (isAvailable) {
                    mExecutorService.execute(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            MultipartBody.Builder formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("per_page", String.valueOf(per_page))
                                    .addFormDataPart("page", String.valueOf(page))
                                    .addFormDataPart("langu", AppLanguage.getLanguage(mContext));
                            if (slug != null) {
                                Log.e("slug//", slug);
                                formBody.addFormDataPart("slug", slug);
                            }

                            RequestBody requestBody = formBody.build();
                            final OkHttpClient client = new OkHttpClient();
                            okhttp3.Request request = new okhttp3.Request.Builder().url(FontManager.URL
                                    + "products/category").post(requestBody).build();
                            final okhttp3.Response response;
                            try {
                                response = client.newCall(request).execute();
                                String response_data = Objects.requireNonNull(response.body()).string();
                                Log.e("products", response_data);
                                if (response_data != null) {
                                    try {
                                        final JSONObject jsonObject = new JSONObject(response_data);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    String status = jsonObject.getString("status");
                                                    if (TextUtils.equals(status, "true")) {
                                                        if (jsonObject.has("results_ar")) {
                                                            String results = jsonObject.getString("results_ar");
                                                            JSONArray jsonArray = new JSONArray(results);
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                                String lang = jsonObject1.getString("lang");
                                                                String id = jsonObject1.getString("id");
                                                                String name = jsonObject1.getString("name");
                                                                String price = jsonObject1.getString("price");
                                                                String image = jsonObject1.getString("image");
                                                                String stock_status = jsonObject1.getString("stock_status");
                                                                String description = jsonObject1.getString("description");
                                                                String imageUrl;
                                                                if (TextUtils.equals(image, "false")) {
                                                                    imageUrl = "false";
                                                                } else {
                                                                    JSONArray imageArray = new JSONArray(image);
                                                                    imageUrl = imageArray.getString(0);
                                                                }
                                                                String date_created = jsonObject1.getString("date_created");
                                                                if (!TextUtils.equals(date_created, "null")) {
                                                                    JSONObject dateJson = new JSONObject(date_created);
                                                                    String date = dateJson.getString("date");
                                                                    Sales sales = new Sales(id, imageUrl, price, name, lang, date, description, stock_status);
                                                                    callback.onProductDone(sales);
                                                                }
                                                            }
                                                        } else if (jsonObject.has("results_en")) {
                                                            String results = jsonObject.getString("results_en");
                                                            JSONArray jsonArray = new JSONArray(results);
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                                String lang = jsonObject1.getString("lang");
                                                                String id = jsonObject1.getString("id");
                                                                String name = jsonObject1.getString("name");
                                                                String price = jsonObject1.getString("price");
                                                                String image = jsonObject1.getString("image");
                                                                String stock_status = jsonObject1.getString("stock_status");
                                                                String description = jsonObject1.getString("description");
                                                                String imageUrl;
                                                                if (TextUtils.equals(image, "false")) {
                                                                    imageUrl = "false";
                                                                } else {
                                                                    JSONArray imageArray = new JSONArray(image);
                                                                    imageUrl = imageArray.getString(0);
                                                                }
                                                                String date_created = jsonObject1.getString("date_created");
                                                                if (!TextUtils.equals(date_created, "null")) {
                                                                    JSONObject dateJson = new JSONObject(date_created);
                                                                    String date = dateJson.getString("date");
                                                                    Sales sales = new Sales(id, imageUrl, price, name, lang, date, description, stock_status);
                                                                    callback.onProductDone(sales);
                                                                }
                                                            }
                                                        }
                                                    } else if (TextUtils.equals(status, "false")) {
                                                        callback.onError(mContext.getString(R.string.donot_have_product));
                                                    }
                                                } catch (final JSONException e) {
                                                    e.printStackTrace();
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            callback.onError(mContext.getString(R.string.something_wrong));
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (final Exception e) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                assert callback != null;
                                                callback.onError(mContext.getString(R.string.something_wrong));

                                            }
                                        });
                                    }
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onError(mContext.getString(R.string.no_internet_connection));
                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError(mContext.getString(R.string.something_wrong));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(mContext.getString(R.string.no_internet_connection));
                        }
                    });

                }
            }
        });
    }
}
