package com.project.creativly.canshopproject.callback;

import com.project.creativly.canshopproject.model.OrderDetails;
import com.project.creativly.canshopproject.model.User;

public interface ConnectionManagerInterface {

    void login(User user, LoginCallback callback);

    void register(User user, RegisterCallback callback);

    void productOffers(final int per_page, final int page, final OfferCallback callback);

    void productLatest(final int per_page, final int page, final ProductCallback callback);

    void showProfile(final LoginCallback callback);

    void orderList(final OrderCallback callback);

    void editProfile(final User user, final InstallCallback callback);

    void placeOrder(final OrderDetails orderDetails, final InstallCallback callback);

    void orderListShow(final String id, final OrderDetailsCallback callback);

    void search(final String search, final ProductCallback callback);

    void getCategories(final CategoriesCallback callback);

    void productCategories(final int per_page, final int page, final String slug, final ProductCallback callback);
}