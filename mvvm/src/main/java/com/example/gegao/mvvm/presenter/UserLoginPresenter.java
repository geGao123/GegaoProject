package com.example.gegao.mvvm.presenter;

import android.os.Handler;

import com.example.gegao.mvvm.bean.UserInfo;
import com.example.gegao.mvvm.biz.IUserBiz;
import com.example.gegao.mvvm.biz.OnLoginListener;
import com.example.gegao.mvvm.biz.UserBiz;
import com.example.gegao.mvvm.view.IUserLoginView;


public class UserLoginPresenter {
    private IUserBiz iUserBiz;
    private IUserLoginView userLoginView;
    private Handler handler = new Handler();

    public UserLoginPresenter(IUserLoginView userLoginView){
        this.userLoginView = userLoginView;
        iUserBiz = new UserBiz();
    }

    public void login(){
        iUserBiz.login(userLoginView.getUsername(), userLoginView.getPassword(), new OnLoginListener() {
            @Override
            public void LoginSuccess(final UserInfo userInfo) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.toShowLoginSuccess(userInfo);
                    }
                });
            }

            @Override
            public void loginfail(final String errorMessage) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        userLoginView.showFailedError(errorMessage);
                    }
                });
            }
        });
    }
}
