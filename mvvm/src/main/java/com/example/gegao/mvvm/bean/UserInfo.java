package com.example.gegao.mvvm.bean;

/**
 * Created by zhangzhanzhong on 2015/08/21.
 */
public class UserInfo {

    private String uId;//用户id
    private String userName;//用户名
    private String passWord;//密码

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
