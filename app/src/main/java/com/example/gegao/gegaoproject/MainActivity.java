package com.example.gegao.gegaoproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import com.example.gegao.gegaoproject.util.MultiThreadDownload;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int pageID = 1;
    private final String URL = "http://www.wmpic.me/tupian/photo";
    private TextView tv_percent;
    private Handler handler;
    private MultiThreadDownload multiThreadDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_percent = (TextView) findViewById(R.id.tv_percent);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//                if (msg.what == 1) {
                    int percent = msg.arg1;
                    tv_percent.setText(percent + "%");
//                }
            }
        };
        multiThreadDownload = new MultiThreadDownload(handler,
                "http://apkdown.androidnet.cn/2015/08/com.tencent.mobileqq_264.apk",
                "");
//        multiThreadDownload = new MultiThreadDownload(handler,
//                "http://a.hiphotos.baidu.com/album/r=90/sign=2ba51d21b2b7d0a27ec9089dcaefda61/4610b912c8fcc3ce456cb8a09245d688d53f20f0.jpg",
//                "");
        multiThreadDownload.run();

    }

    @Override
    public void onBackPressed() {
        multiThreadDownload.interruptAll();
        super.onBackPressed();
    }
}
