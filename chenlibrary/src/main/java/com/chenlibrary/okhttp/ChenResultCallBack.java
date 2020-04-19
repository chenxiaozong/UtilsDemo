package com.chenlibrary.okhttp;

/**
 * okHttp的回调监听
 */
public abstract class ChenResultCallBack<T extends BaseBean> {
    public static final String TAG = ChenResultCallBack.class.getSimpleName();

    //后台的状态码             成功                  身份认证过期       onFailed
    public static final int CODE_SUCCESS = 0, CODE_GUOQI = 20100, CODE_FAILED = -10000;

    //其他状态码                  json解析异常                网络连接异常
    public static final int CODE_JSONEXCEPTION = -999, CODE_CONNECTXCEPTEION = -998, CODE_200 = 200, CODE_404 = 404, CODE_500 = 500, CODE_502 = 502,CODE_SERVER_RESPONSE =1000;

    /**
     * 链接失败或者解析数据失败走此方法,
     * @param baseBean
     */
    public void onDataError(BaseBean baseBean) {//连接失败返回
        String otherMsg;

        switch (baseBean.code) {
            case CODE_JSONEXCEPTION:
                otherMsg = "数据解析异常,请更新版本或稍后再试!";
                break;
            default:
                otherMsg = "其他错误:" + baseBean.httpCode + " bean.code" + baseBean.code;
                break;
        }

        ChenOkHttp.printOkhttpLog(baseBean,false, otherMsg,"ON_DATA_ERROR");
        this.onError((T) baseBean);
    }


    /**
     * 服务端错误,只在日志中标出,不在回调中抛出
     * @param baseBean
     */
    public void onServiceError(BaseBean baseBean) {//服务器返回非success,例如message="无效请求"
        String netMsg = "";
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

        ChenOkHttp.printOkhttpLog(baseBean,false, netMsg,"ON_SERVICE_ERROR");
        this.onError((T) baseBean);
    }

    //此方法必走
    public void onNext(BaseBean bean) {
    }

    public abstract void onSuccess(T bean);

    /**
     * onError 方法: 包含onServiceError &onNetworkError
     * 如果业务需要获取该状态,重写此方法即可
     * @param bean
     */
    public void  onError(T bean){
        //TODO   如需要处理onError 调用时在回调中重写该方法
    }


    /**
     * 此bean是否是一条成功的数据
     */
    public static boolean isSuccessedBean(BaseBean bean) {
        return bean.httpCode == CODE_200 && bean.code == CODE_SUCCESS;
    }
}