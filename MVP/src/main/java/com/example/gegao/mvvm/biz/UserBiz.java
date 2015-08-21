package com.example.gegao.mvvm.biz;

import android.text.TextUtils;

import com.example.gegao.mvvm.bean.UserInfo;

import java.util.UUID;

/**
 * Created by zhangzhanzhong on 2015/08/21.
 */
public class UserBiz implements IUserBiz {
    @Override
    public void login(final String username, final String password, final OnLoginListener onLoginListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    if (TextUtils.isEmpty(username)) {
                        onLoginListener.loginfail("请输入账号");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        onLoginListener.loginfail("请输入密码");
                        return;
                    }
                    if (!username.equals("lch") || !password.equals("123456")) {
                        onLoginListener.loginfail("账号或者密码错误");
                        return;
                    }
                    UserInfo userInfo = new UserInfo();
                    userInfo.setuId(UUID.randomUUID().toString().trim());
                    userInfo.setUserName("lch");
                    onLoginListener.LoginSuccess(userInfo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    onLoginListener.loginfail("未知错误");
                }
            }
        }).start();
    }
}
