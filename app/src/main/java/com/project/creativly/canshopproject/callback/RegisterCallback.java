package com.project.creativly.canshopproject.callback;


import com.project.creativly.canshopproject.model.User;

public interface RegisterCallback extends ErrorHandlerInterface {
    void onUserRegisterDone(User user);
}
