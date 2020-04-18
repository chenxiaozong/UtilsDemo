package com.chen.utilsdemo.utils.net;


import android.content.Context;
import android.text.TextUtils;

import com.chen.utilsdemo.utils.ChenLog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    //json中的字段
    public static final String KEY_USERTOKEN = "userToken", KEY_USERID = "userId", KEY_PAGENUM = "pageNum", KEY_PAGESIZE = "pageSize", KEY_STOREID = "storeId";

    public static final OkHttpClient mClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build();

    //类型 utf-8
    public static final MediaType mMediaType = MediaType.parse("application/json;charset=UTF-8");

    ///////////////////////////////////////////////////////////////////////////
    // 以下是http公共方法
    ///////////////////////////////////////////////////////////////////////////


    public static ChenOkHttpUtils chenOkHttpUtilInstance;
    private Context mContext;

    public ChenOkHttpUtils(Context mContext) {
        this.mContext = mContext;
    }

    public static ChenOkHttpUtils getInstance(Context context) {
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
    }


    /**
     * 通过map 添加get参数
     *
     * @param url
     * @param params
     * @return
     */
    public ChenRequestBuilder doDet(final String url, HashMap<String, String> params) {
        ChenRequestBuilder builder = new ChenRequestBuilder();
        String pUrl = putGedPrarams(url, params);
        ChenLog.i("doGet [url]:", pUrl);
        builder.tag(url)//打tag
                .url(pUrl);
        return builder;
    }

    /**
     * 不需要加get参数调此方法
     *
     * @param url
     * @return
     */
    public ChenRequestBuilder doDet(final String url) {
        ChenRequestBuilder builder = new ChenRequestBuilder();
        ChenLog.i("doGet [url]:", url);
        builder.tag(url).url(url);
        ChenLog.i("doDet:", url, " builder:", builder);
        return builder;
    }

    private String putGedPrarams(String url, HashMap<String, String> paramsMap) {
        if (paramsMap == null || paramsMap.size() <= 0) {
            return url;
        }
        StringBuilder stringBuilder = null;
        for (Map.Entry<String, String> param : paramsMap.entrySet()) {
            String value = param.getValue();
            String key = param.getKey();
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                continue;
            }

            if (stringBuilder == null) {
                stringBuilder = new StringBuilder().append("?");
            } else {
                stringBuilder.append("&");
            }
            stringBuilder.append(key).append("=").append(value);
        }

        if (stringBuilder == null) {
            return url;
        } else {
            return url + stringBuilder.toString();
        }
    }

    public interface ResultListener {
        void strResult(NetUtils.Result result);
    }

    public static class ChenRequestBuilder extends Request.Builder {
        private String urlTag;

        @Override
        public Request.Builder tag(Object tag) {
            this.urlTag = (String) tag;
            return super.tag(tag);
        }

        public ChenRequestBuilder addHeaders(HashMap<String, String> headersMap) {
            if (headersMap == null && headersMap.size() <= 0) {
                return this;
            }
            for (Map.Entry<String, String> header : headersMap.entrySet()) {
                String key = header.getKey();
                String value = header.getValue();
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    return this;
                }
                this.addHeader(key, value);
            }
            return this;
        }

        public <T extends BaseBean> void onResult(final ChenOKHttpListener<BaseBean> callBack, final Class<T> mClass) {
            Request request = this.build();
            HttpUrl url = request.url();
            ChenLog.i("onResult:", url, " builder:", this);
            Call call = mClient.newCall(request);
            call.enqueue(new ResponseCallBack() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    postResultBean(callBack, new BaseBean(ChenOKHttpListener.CODE_CONNECTXCEPTEION, e.getLocalizedMessage(), null, urlTag, null));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    if (response.code() == ChenOKHttpListener.CODE_200) {
                        try {
//                            BaseBean bean = newg.parseObject(body, mClass, Feature.SupportNonPublicField);//支持私有变量
                            BaseBean bean = new BaseBean();
                            if (mClass != null) {//需要解析时使用gson解析
                                bean = new Gson().fromJson(body, mClass);
                            }
                            bean.httpCode = response.code();
                            bean.response = body;
                            bean.httpUrl = urlTag;
                            bean.code = ChenOKHttpListener.CODE_SUCCESS;

                            postResultBean(callBack, bean);
                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseBean baseBean = new BaseBean(response.code(), null, body, urlTag, response.headers());
                            baseBean.code = ChenOKHttpListener.CODE_JSONEXCEPTION;
                            postResultBean(callBack,baseBean );
                        }
                    } else {
                        //服务器返回其他状态
                        postResultBean(callBack, new BaseBean(response.code(), response.message(), body, urlTag, response.headers()));
                    }


                }
            });
        }

        private void postResultBean(ChenOKHttpListener<BaseBean> callBack, BaseBean baseBean) {
            //如果activity要求丢弃数据
            if (callBack == null) return;
            if (baseBean.httpCode == ChenOKHttpListener.CODE_200 ) {
                if (baseBean.code ==ChenOKHttpListener.CODE_SUCCESS){
                    ChenLog.i("onSuccess :\n"
                            ,">--------onSuccess-------"
                            ,"\nhttpUrl:",baseBean.httpUrl
                            ,"\nhttpCode:",baseBean.httpCode
                            ,"\ncode:", baseBean.code
                            ,"\n>----------------------"
                    );
                    callBack.onSuccess(baseBean);
                }else {
                    callBack.onNetworkError(baseBean);
                }
            } else {
                callBack.onServiceError(baseBean);
            }
            callBack.onNext(baseBean);
        }
    }
}
