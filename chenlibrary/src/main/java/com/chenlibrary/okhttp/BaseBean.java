package com.chenlibrary.okhttp;

import java.io.Serializable;

import okhttp3.Headers;

/**
 * !!!子类不要重写code,message等字段,否则http请求回来永远都是error
 * 写的时候先继承,再用GsonFormat就不会被重写了
 * http请求所需要的基类,请求的bean必须继承于它
 */
public class BaseBean implements Serializable {
    public static final String TAG = "BaseBean";
    public int code;//后台给的状态码 && 自定义code 比如json解析失败等
    public String message, detailMessage;//错误语,错误详情
    public int httpCode;//http链接的状态码
    public String body, httpUrl;
    public String urlTag;// 保存请求时的url 转码前
    public Headers httpHeader;//请求回来的头
    public String method = "";

    public BaseBean(int code, String message, int httpCode, String body, String httpUrl, String urlTag, Headers httpHeader, String method) {
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
        this.body = body;
        this.httpUrl = httpUrl;
        this.urlTag = urlTag;
        this.httpHeader = httpHeader;
        this.method = method;
    }
    public BaseBean() {
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", detailMessage='" + detailMessage + '\'' +
                ", httpCode=" + httpCode +
                ", body='" + body + '\'' +
                ", httpUrl='" + httpUrl + '\'' +
                ", urlTag='" + urlTag + '\'' +
                ", httpHeader=" + httpHeader +
                ", method='" + method + '\'' +
                '}';
    }
}
