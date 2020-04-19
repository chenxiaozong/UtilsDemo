package com.chen.utilsdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.chen.utilsdemo.bean.UserInfoJsonBean;
import com.chenlibrary.okhttp.BaseBean;
import com.chenlibrary.okhttp.ChenOkHttp;
import com.chenlibrary.okhttp.ChenResultCallBack;
import com.chenlibrary.utils.ChenLog;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

   private static final String url = "http://192.168.1.107:8080/test_war/userInfo.json";

    private static final String videoUrl = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testPost();

        testGet();



    }

    private void testPost() {

        /**
         * post Body 参数
         */
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("chen", "chenxzong");
        bodyMap.put("age", 13);
        bodyMap.put("addr", "青岛市李沧区");
        bodyMap.put("girl", false);
        String jsonBody = "{\n" +
                "        \"name\": \"chenxzong\",\n" +
                "        \"age\": 30,\n" +
                "        \"info\": \"山东省青岛市李沧区\"\n" +
                "    }";

        /**
         * headers 参数
         */
        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("token", "token");

        ChenOkHttp.getInstance(this)
                .doPost(url)
                .addHeaders(headersMap)
                .addMapBody(bodyMap)
                .addJsonBody(jsonBody)
                .onResult(null, new ChenResultCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean bean) {

                    }
                });
    }


    public void testGet(){



        /**
         * get 参数键值对,内部可根据键值对拼接成 xxx?key=val&key2=val2.....
         */
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("chen", "chenxzong");
        bodyMap.put("age", 13);
        bodyMap.put("addr", "青岛市李沧区");
        bodyMap.put("girl", false);
        String jsonBody = "string";


        /**
         * header参数
         */
        HashMap<String, String> headersMap = new HashMap<>();
        headersMap.put("token", "token");

        /**
         * 需要拼接 url参数
         */
//        ChenOkHttp.getInstance(this)
//                .doGet(url, bodyMap)
//                .addHeaders(headersMap)
//                .onResult(UserInfoJsonBean.class, new ChenResultCallBack<BaseBean>() {
//                    @Override
//                    public void onSuccess(BaseBean bean) {
//                    }
//
//                });

        ChenOkHttp.getInstance(this).doGet(url);

        ChenOkHttp.getInstance(this).doGet(videoUrl).onResult(UserInfoJsonBean.class, new ChenResultCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {
                ChenLog.i("onSuccess:",bean);
            }

            @Override
            public void onError(BaseBean bean) {
            }
        });
    }




}
