package com.example.gegao.mvvm.biz;

/**
 * Created by zhangzhanzhong on 2015/08/21.
 */
public interface IUserBiz {
    void login(String username, String password, OnLoginListener onLoginListener);
}
