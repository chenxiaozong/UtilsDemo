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
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
public class ChenOkHttp<T> {
    public static final String TAG = ChenOkHttp.class.getSimpleName();
    private static final MediaType MEDIATYPE_JSON = MediaType.parse("application/json; charset=utf-8");
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


    public static ChenOkHttp chenOkHttpUtilInstance;
    private Context mContext;

    public ChenOkHttp(Context mContext) {
        this.mContext = mContext;
    }

    public static ChenOkHttp getInstance(Context context) {
        if (chenOkHttpUtilInstance == null) {
            synchronized (NetUtils.class) {
                if (chenOkHttpUtilInstance == null) {
                    chenOkHttpUtilInstance = new ChenOkHttp(context.getApplicationContext());
                }
            }
        }
        return chenOkHttpUtilInstance;
    }


    public interface ResponseCallBack extends Callback {
    }


    /**
     * 通过map 添加get参数
     * 内部可根据键值对拼接成 xxx?key=val&key2=val2.....
     *
     * @param url    :要求此时的url 不包含 ?
     * @param params
     * @return
     */
    public ChenBuilder doGet(final String url, Map<String, T> params) {
        if (url.contains("?") || url.contains("&")) {
            throw new IllegalArgumentException("url can not contains ? or && ");
        }
        String pUrl = putGedPrarams(url, params);//添加get 参数
        ChenLog.i("putGedPrarams:", pUrl);
        return this.doGet(pUrl);
    }

    /**
     * 不需要 通过map 拼接 get参数调此方法
     *
     * @param url
     * @return
     */
    public ChenBuilder doGet(final String url) {
        ChenBuilder builder = new ChenBuilder();
        builder.setMethod(ChenBuilder.GET);
        builder.tag(url).url(url);
        return builder;
    }


    /**
     * 不需要 通过map 拼接 get参数调此方法
     *
     * @param url
     * @return
     */
    public ChenBuilder doPost(final String url) {
        ChenBuilder builder = new ChenBuilder();
        builder.setMethod(ChenBuilder.POST);
        builder.tag(url).url(url);
        return builder;
    }

    private String putGedPrarams(String url, Map<String, T> paramsMap) {
        if (paramsMap == null || paramsMap.size() <= 0) {
            return url;
        }
        StringBuilder stringBuilder = null;
        for (Map.Entry<String, T> param : paramsMap.entrySet()) {
            T value = param.getValue();
            String key = param.getKey();
            if (TextUtils.isEmpty(key)) {
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

    public class ChenBuilder extends Request.Builder {
        private int method = GET;       //默认方法
        private static final int POST = 1001;   // postt
        private static final int GET = 1002;    //get
        private String urlTag;//通常存储保存 转码前的url

        public int getMethod() {
            return method;
        }

        private void setMethod(int method) {
            this.method = method;
        }

        @Override
        public Request.Builder tag(Object tag) {
            this.urlTag = (String) tag;
            return super.tag(tag);
        }

        /**
         * 添加的header参数 键值对必须是 String :String
         *
         * @param headersMap
         * @return
         */
        public ChenBuilder addHeaders(HashMap<String, String> headersMap) {
            if (headersMap == null && headersMap.size() <= 0) {
                return this;
            }
            for (Map.Entry<String, String> header : headersMap.entrySet()) {
                String key = header.getKey();
                String value = header.getValue();
                if (TextUtils.isEmpty(key)) {
                    continue;//忽略空的header 值
                }
                this.addHeader(key, value);
            }
            return this;
        }


        /**
         * 网络请求的返回回调
         *
         * @param mClass         : 返回body 对应的bean对象 如果为null 则不进行json解析,
         * @param resultCallBack
         * @param <T>            :response.body 对应的json bean类型
         */
        public <T extends BaseBean> void onResult(final Class<T> mClass, final ChenResultCallBack<BaseBean> resultCallBack) {
            final Request request = this.build();

            final String httpUrl = request.url().toString();
            final Headers headers = request.headers();

            Call call = mClient.newCall(request);
            call.enqueue(new ResponseCallBack() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    BaseBean baseBean = new BaseBean(ChenResultCallBack.CODE_CONNECTXCEPTEION,e.getMessage(),ChenResultCallBack.CODE_FAILED,null,httpUrl,urlTag, headers);
                    postResultBean(resultCallBack, baseBean);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    if (response.code() == ChenResultCallBack.CODE_200) {
                        try {
//                            BaseBean bean = newg.parseObject(body, mClass, Feature.SupportNonPublicField);//支持私有变量
                            BaseBean bean = new BaseBean();
                            if (mClass != null) {//需要解析时使用gson解析
                                bean = new Gson().fromJson(body, mClass);
                            }
                            bean.httpCode = response.code();
                            bean.body = body ;
                            bean.httpUrl = httpUrl;
                            bean.urlTag = urlTag;
                            bean.code = ChenResultCallBack.CODE_SUCCESS;

                            postResultBean(resultCallBack, bean);
                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseBean baseBean = new BaseBean(ChenResultCallBack.CODE_JSONEXCEPTION,e.getMessage(),response.code(),body,httpUrl,urlTag, response.headers());
                            postResultBean(resultCallBack, baseBean);
                        }
                    } else {
                        //服务器返回其他状态
                        BaseBean baseBean = new BaseBean(ChenResultCallBack.CODE_SERVER_RESPONSE,response.message(),response.code(),body,httpUrl,urlTag, response.headers());

                        postResultBean(resultCallBack,baseBean );
                    }


                }
            });
        }

        /**
         * 处理返回的BaseBean 根据错误码 抛出对应的错误
         *
         * @param callBack
         * @param baseBean
         */
        private void postResultBean(ChenResultCallBack<BaseBean> callBack, BaseBean baseBean) {
            //如果activity要求丢弃数据
            if (callBack == null) return;
            if (baseBean.httpCode == ChenResultCallBack.CODE_200) {

                // 返回数据正常|| 解析正常 返回success


                if (baseBean.code == ChenResultCallBack.CODE_SUCCESS) {
                    printOkhttpLog(baseBean, true, "CODE_SUCCESS","CODE_SUCCESS");
                    callBack.onSuccess(baseBean);
                } else {
                    callBack.onDataError(baseBean);
                }
            } else {
                callBack.onServiceError(baseBean);
            }
            callBack.onNext(baseBean);
        }

        /**
         * 添加MAP格式的body
         *
         * @param map
         * @return
         */
        public ChenBuilder addMapBody(Map<String, Object> map) {
            if (map == null || map.isEmpty()) {
                throw new NullPointerException("map == null");
            }
            try {
                String json = new Gson().toJson(map);
                ChenLog.i("toJson:>>>", json);
                RequestBody body = RequestBody.create(MEDIATYPE_JSON, json);
                this.post(body);
            } catch (Throwable throwable) {
                throw throwable;
            } finally {
                return this;
            }

        }

        /**
         * 添加Json格式的body
         *
         * @param jsonBody
         * @return
         */
        public ChenBuilder addJsonBody(String jsonBody) {
            if (TextUtils.isEmpty(jsonBody)) {
                throw new NullPointerException("jsonBody is null or empty");
            }

            //json格式body - utf-8编码
            RequestBody body = RequestBody.create(MEDIATYPE_JSON, jsonBody);
            this.post(body);
            return this;
        }

    }

    public static void printOkhttpLog(BaseBean baseBean, boolean isSucces, String info,String type) {

        String header = ">isSucces = "+isSucces+"<";
        if (!TextUtils.isEmpty(type)){
            header = type;
        }

        if (isSucces) {
            ChenLog.i(TAG,
                    "\n============================",header,"============================",
                    "\n [HttpUrl ]:", baseBean.httpUrl,
                    "\n [UrlTag  ]:", baseBean.urlTag,
                    "\n [HttpCode]:", baseBean.httpCode,
                    "\n [BeanCode]:", baseBean.code,
                    "\n [Headers ]:", baseBean.httpHeader,
                    "\n [ErrorMsg]:", info,
                    "\n ---------------------------------------------------------------",
                    "\n [BeanBody]:", baseBean.body,
                    "\n ---------------------------------------------------------------"
            );
        } else {
            ChenLog.e(TAG,
                    "\n============================",header,"============================",
                    "\n [HttpUrl ]:", baseBean.httpUrl,
                    "\n [UrlTag  ]:", baseBean.urlTag,
                    "\n [HttpCode]:", baseBean.httpCode,
                    "\n [BeanCode]:", baseBean.code,
                    "\n [Headers ]:", baseBean.httpHeader,
                    "\n [ErrorMsg]:", info,
                    "\n ---------------------------------------------------------------",
                    "\n [BeanBody]:", baseBean.body,
                    "\n ---------------------------------------------------------------"
            );


        }
    }


}
