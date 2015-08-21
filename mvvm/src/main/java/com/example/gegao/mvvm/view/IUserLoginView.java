package com.example.gegao.mvvm.view;

import com.example.gegao.mvvm.bean.UserInfo;

/**
 * Created by zhangzhanzhong on 2015/08/21.
 */
public interface IUserLoginView {

    String getUsername();

    String getPassword();

    void showFailedError(String errorInfo);

    void toShowLoginSuccess(UserInfo userInfo);
}
