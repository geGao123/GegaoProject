package com.example.gegao.mvvm.biz;

import com.example.gegao.mvvm.bean.UserInfo;

/**
 * Created by zhangzhanzhong on 2015/08/21.
 */
public interface OnLoginListener {

    //登陆成功的回掉
    void LoginSuccess(UserInfo userInfo);

    //登陆失败的回掉
    void loginfail(String errorMessage);
}
