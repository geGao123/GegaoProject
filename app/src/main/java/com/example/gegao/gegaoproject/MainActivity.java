package com.example.gegao.gegaoproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gegao.gegaoproject.util.HtmlTool;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int pageID =1;
    private final String URL= "http://www.wmpic.me/tupian/photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        List<Map<String, String>> xiezhenList = HtmlTool.getXiezhenList(URL, ++pageID);
    }

}
