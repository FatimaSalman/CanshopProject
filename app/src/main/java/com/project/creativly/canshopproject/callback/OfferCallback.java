package com.project.creativly.canshopproject.callback;


import com.project.creativly.canshopproject.model.Offer;

public interface OfferCallback extends ErrorHandlerInterface {
    void onOfferDone(Offer offer);
}
