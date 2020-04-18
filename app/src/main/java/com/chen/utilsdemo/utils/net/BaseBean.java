package com.chen.utilsdemo.utils.net;

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
    public String response, httpUrl;
    public Headers httpHeader;//请求回来的头

    public BaseBean() {
    }

    public BaseBean(int httpCode, String message, String response, String httpUrl, Headers httpHeader) {
        this.httpCode = httpCode;
        this.message = message;
        this.response = response;
        this.httpUrl = httpUrl;
        this.httpHeader = httpHeader;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", detailMessage='" + detailMessage + '\'' +
                ", httpCode=" + httpCode +
                ", response='" + response + '\'' +
                ", httpUrl='" + httpUrl + '\'' +
                ", httpHeader=" + httpHeader +
                '}';
    }
}
