package com.project.creativly.canshopproject.callback;


import com.project.creativly.canshopproject.model.Sales;

public interface ProductCallback extends ErrorHandlerInterface {
    void onProductDone(Sales sales);
}
