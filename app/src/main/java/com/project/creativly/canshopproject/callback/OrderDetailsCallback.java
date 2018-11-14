package com.project.creativly.canshopproject.callback;


import com.project.creativly.canshopproject.model.OrderDetails;

public interface OrderDetailsCallback extends ErrorHandlerInterface {
    void onOrderDone(OrderDetails order);
}
