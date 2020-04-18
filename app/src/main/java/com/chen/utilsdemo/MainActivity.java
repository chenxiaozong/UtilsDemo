package com.chen.utilsdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.chen.utilsdemo.bean.VideoData;
import com.chen.utilsdemo.utils.ChenLog;
import com.chen.utilsdemo.utils.net.BaseBean;
import com.chen.utilsdemo.utils.net.ChenOKHttpListener;
import com.chen.utilsdemo.utils.net.ChenOkHttpUtils;

import java.util.HashMap;
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

        String url = "http://192.168.1.107:8080/test_war/userInfo.json";

        String videoUrl = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";


        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("token", "token");

        ChenOkHttpUtils.getInstance(this)
                .doDet(videoUrl)
                .onResult(new ChenOKHttpListener<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        VideoData infoBean = (VideoData) bean;
//                        ChenLog.i(infoBean);
                        ChenLog.i("success:",infoBean);
                    }
                }, VideoData.class);

    }
}
