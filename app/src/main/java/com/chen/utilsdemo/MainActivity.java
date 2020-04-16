package com.chen.utilsdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.chen.utilsdemo.utils.ChenLog;
import com.chen.utilsdemo.utils.net.NetUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
            .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "http://192.168.1.103:8080/test_war/userInfo.json";

        String videoUrl = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
//        ChenLog.i("test","oncreate");
        NetUtils.getInstance(this).doDet(new NetUtils.ResultListener() {
            @Override
            public void strResult(NetUtils.Result result) {
                ChenLog.i("strResult:",result);
            }
        }, url);


    }
}
