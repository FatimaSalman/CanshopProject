package com.project.creativly.canshopproject.callback;


import com.project.creativly.canshopproject.model.Order;

public interface OrderCallback extends ErrorHandlerInterface {
    void onOrderDone(Order order);
}
