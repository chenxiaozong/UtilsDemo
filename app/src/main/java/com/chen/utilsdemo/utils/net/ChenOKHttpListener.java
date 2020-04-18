package com.chen.utilsdemo.utils.net;

import com.chen.utilsdemo.utils.ChenLog;

/**
 * okHttp的回调监听
 */
public abstract class ChenOKHttpListener<T extends BaseBean> {
    public static final String TAG = ChenOKHttpListener.class.getSimpleName();

    //后台的状态码             成功                  身份认证过期
    public static final int CODE_SUCCESS = 0, CODE_GUOQI = 20100, CODE_FAILED = 20000;

    //其他状态码                  json解析异常                网络连接异常
    public static final int CODE_JSONEXCEPTION = -999, CODE_CONNECTXCEPTEION = -998, CODE_200 = 200, CODE_404 = 404, CODE_500 = 500, CODE_502 = 502;

    public void onNetworkError(BaseBean baseBean) {//连接失败返回
        String netMsg;
        String otherMsg;

        switch (baseBean.code) {
            case CODE_JSONEXCEPTION:
                otherMsg = "数据解析异常,请更新版本或稍后再试!";
                break;
            default:
                otherMsg = "其他错误:" + baseBean.httpCode + " bean.code" + baseBean.code;
                break;
        }


        switch (baseBean.httpCode) {
            case CODE_CONNECTXCEPTEION:
                netMsg = "网络连接失败,请检查网络再试!";
                break;
            case CODE_404:
                netMsg = "网址不存在,请更新版本或稍后再试!";
                break;
            case CODE_500:
                netMsg = "服务器异常,请稍后再试!";
                break;
            case CODE_502:
                netMsg = "服务器打了个哈欠,等一会再看看吧";
                break;
            case CODE_200:
                netMsg = "请求成功了!! 看看是不是其他错误 ^_^";
                break;
            default:
                netMsg = "连接失败!错误码:" + baseBean.httpCode + " bean.code" + baseBean.code;
                break;
        }
//        ChenUtils.toastInstance(st);
        ChenLog.i("onNetworkError:","\n----------\n NetMsg:", netMsg,
                " \n OtherMsg:", otherMsg, "\n----------\n", baseBean);

    }

    public void onServiceError(BaseBean baseBean) {//服务器返回非success,例如message="无效请求"
//        ChenUtils.toastInstance(TextUtils.isEmpty(baseBean.message) ? "获取信息失败!" : BuildConfig.DEBUG ? (baseBean.message + baseBean.detailMessage) : baseBean.message);
        ChenLog.i("onServiceError:", baseBean);
    }

    //此方法必走
    public void onNext(BaseBean bean) {
    }

    public abstract void onSuccess(T bean);

    /**
     * 此bean是否是一条成功的数据
     */
    public static boolean isSuccessedBean(BaseBean bean) {
        return bean.httpCode == CODE_200 && bean.code == CODE_SUCCESS;
    }
}