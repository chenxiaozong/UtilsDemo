package com.chenlibrary.okhttp;


import android.text.TextUtils;

import com.chenlibrary.utils.ChenLog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.chenlibrary.okhttp.ChenOkHttp.MEDIATYPE_JSON;
import static com.chenlibrary.okhttp.ChenOkHttp.mClient;
import static com.chenlibrary.okhttp.ChenOkHttp.printOkhttpLog;

/**
 * 版权：chenxiaozong 版权所有
 * -------------------------
 * 作者：chenxiaozong
 * 邮箱：chenxzong@qq.com
 * 代码：https://gitee.com/chenxiaozong
 * 版本：1.0
 * 日期：2020/4/19 7:38 PM
 * 描述：com.chenlibrary.okhttp/.java
 */
public  class ChenBuilder  extends Request.Builder{
    public static final String TAG = ChenBuilder.class.getSimpleName();
    private int method = ChenOkHttp.GET;       //默认方法

    private String urlTag;//通常存储保存 转码前的url

    public int getMethod() {
        return method;
    }

    protected void setMethod(int method) {
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
        final String method = request.method();


        Call call = mClient.newCall(request);
        call.enqueue(new ChenOkHttp.ResponseCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                BaseBean baseBean = new BaseBean(ChenResultCallBack.CODE_CONNECTXCEPTEION,e.getMessage(),ChenResultCallBack.CODE_FAILED,null,httpUrl,urlTag, headers,method);
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
                        bean.method = method;

                        postResultBean(resultCallBack, bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        BaseBean baseBean = new BaseBean(ChenResultCallBack.CODE_JSONEXCEPTION,e.getMessage(),response.code(),body,httpUrl,urlTag, response.headers(),method);
                        postResultBean(resultCallBack, baseBean);
                    }
                } else {
                    //服务器返回其他状态
                    BaseBean baseBean = new BaseBean(ChenResultCallBack.CODE_SERVER_RESPONSE,response.message(),response.code(),body,httpUrl,urlTag, response.headers(),method);

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
            ChenLog.i(TAG,"\n addMapBody > MapBody:", json);
            RequestBody body = RequestBody.create(MEDIATYPE_JSON, json);
            ChenLog.i(TAG,"\n addMapBody  > RequestBody:",body);
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
        ChenLog.i(TAG,"\n addJsonBody > JsonBody:",jsonBody);
        RequestBody body = RequestBody.create(ChenOkHttp.MEDIATYPE_JSON, jsonBody);
        ChenLog.i(TAG,"\n addJsonBody > RequestBody:",body);
        this.post(body);
        return this;
    }
}
