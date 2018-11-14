package com.project.creativly.canshopproject.callback;

import com.project.creativly.canshopproject.model.User;

public interface LoginCallback extends ErrorHandlerInterface {
    void onLoginDone(User user);
}
