package com.chen.utilsdemo.utils.net;


import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * 版权：chenxiaozong 版权所有
 * -------------------------
 * 作者：chenxiaozong
 * 邮箱：chenxzong@qq.com
 * 代码：https://gitee.com/chenxiaozong
 * 版本：1.0
 * 日期：2020/4/18 9:31 AM
 * 描述：com.chen.utilsdemo.utils.net/.java
 */
public class ChenOkHttpUtils {
    public static final String TAG = ChenOkHttpUtils.class.getSimpleName();
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
            .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
            .build();
    public static ChenOkHttpUtils chenOkHttpUtilInstance;
    private Context mContext;
    public ChenOkHttpUtils(Context mContext) {
        this.mContext = mContext;
    }
    public static ChenOkHttpUtils getInstance(Context context){
        if (chenOkHttpUtilInstance == null) {
            synchronized (NetUtils.class) {
                if (chenOkHttpUtilInstance == null) {
                    chenOkHttpUtilInstance = new ChenOkHttpUtils(context.getApplicationContext());
                }
            }
        }
        return chenOkHttpUtilInstance;
    }


    public interface ResponseCallBack extends Callback {
        void responseResult( );
    }


}
