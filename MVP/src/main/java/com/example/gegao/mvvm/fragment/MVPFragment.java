package com.example.gegao.mvvm.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gegao.mvvm.R;
import com.example.gegao.mvvm.bean.UserInfo;
import com.example.gegao.mvvm.presenter.UserLoginPresenter;
import com.example.gegao.mvvm.view.IUserLoginView;

/**
 * Created by zhangzhanzhong on 2015/8/17 0017.
 */
public class MVPFragment extends Fragment implements IUserLoginView {

    private TextView tv_loggedInUserid;
    private EditText et_username;
    private EditText et_password;
    private Button bt_login;
    private UserLoginPresenter userLoginPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mvp, container, false);
        userLoginPresenter = new UserLoginPresenter(this);
        init(rootView);
        initEvent();
        return rootView;
    }

    private void init(View rootView) {
        tv_loggedInUserid = (TextView) rootView.findViewById(R.id.tv_loggedInUserid);
        et_username = (EditText) rootView.findViewById(R.id.et_username);
        et_password = (EditText) rootView.findViewById(R.id.et_password);
        bt_login = (Button) rootView.findViewById(R.id.bt_login);
    }

    private void initEvent() {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLoginPresenter.login();
            }
        });
    }

    @Override
    public String getUsername() {
        return et_username.getEditableText().toString().trim();
    }

    @Override
    public String getPassword() {
        return et_password.getEditableText().toString().trim();
    }

    @Override
    public void showFailedError(String errorInfo) {
        Toast.makeText(getActivity(), errorInfo, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toShowLoginSuccess(UserInfo userInfo) {
        Toast.makeText(getActivity(), "欢迎:" + userInfo.getUserName(), Toast.LENGTH_SHORT).show();
    }
}
