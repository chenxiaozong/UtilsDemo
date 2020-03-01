package com.chen.utilsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.chen.utilsdemo.utils.ChenLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChenLog.i("日志打印，","tag1","info2");
    }
}
