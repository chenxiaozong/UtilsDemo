package com.chen.utilsdemo.utils.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.chen.utilsdemo.utils.ChenLog;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NetUtils {

    public static final String TAG = NetUtils.class.getSimpleName();
    public static final String REQEUST_ERROR = "REQUEST_ERROR";
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
            .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
            .build();
    public static NetUtils netUtilsInstance;
    private Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public NetUtils(Context applicationContext) {
        mContext = applicationContext;
    }


    public static NetUtils getInstance(Context context) {
        if (netUtilsInstance == null) {
            synchronized (NetUtils.class) {
                if (netUtilsInstance == null) {
                    netUtilsInstance = new NetUtils(context.getApplicationContext());
                }
            }
        }
        return netUtilsInstance;
    }

    public interface ResultListener {
        void strResult(Result result);
    }

    public void  doDet(final ResultListener listener, final String url) {
        final Result netResult = new Result(url);
        try {
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    netResult.msg = e.getLocalizedMessage();
                    netResult.code = -1;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String rsBody = response.body().string();
                    ChenLog.i("result.body:",rsBody);
                    netResult.content = rsBody;
                    netResult.code = response.code();
                    netResult.msg = response.message();
                }
            });

        } catch (Throwable e) {
            if (e instanceof SocketTimeoutException) {//超时
                netResult.msg = "SocketTimeoutException";
            } else if (e instanceof ConnectException) {//连接出错
                netResult.msg = "ConnectException";
            } else {
                e.printStackTrace();
                netResult.msg = e.getMessage();
            }
            netResult.code = -1;
        } finally {
            ChenLog.i(TAG, new StringBuilder("doRequest:").append(netResult.toString()).toString());
            if (listener!=null){
                listener.strResult(netResult);
            }

//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    listener.strResult(netResult);
//                }
//            });
        }

    }

    public void getStrResultListern(final ResultListener listener, final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Result netResult = new Result(url);
                try {


                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();


                    netResult.content = response.body().string();
                    netResult.code = response.code();
                    netResult.msg = response.message();


                } catch (Throwable e) {
                    if (e instanceof SocketTimeoutException) {//超时
                        netResult.msg = "SocketTimeoutException";
                    } else if (e instanceof ConnectException) {//连接出错
                        netResult.msg = "ConnectException";
                    } else {
                        e.printStackTrace();
                        netResult.msg = e.getMessage();
                    }

                    netResult.code = -1;

                } finally {

                    ChenLog.i(TAG, new StringBuilder("doRequest:").append(netResult.toString()).toString());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.strResult(netResult);
                        }
                    });
                }
            }
        }).start();
    }

    public final class Result {

        private String url;
        public String msg;
        private String content;
        public int code;

        public Result(String url) {
            this.url = url;
        }

        public Result() {
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getContent() {
            if (TextUtils.isEmpty(content)) {
                return "no data";
            }
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Result{");
            sb.append("url='").append(url).append('\'');
            sb.append(", msg='").append(msg).append('\'');
            sb.append(", content='").append(content).append('\'');
            sb.append(", code=").append(code);
            sb.append('}');
            return sb.toString();
        }
    }


    /**
     * 使用Gson将Json字符串转换为对象
     * 将字符串转换为 对象
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> T JsonToObject(String json, Class<T> type) {
        T result = null;
        boolean empty = TextUtils.isEmpty(json);
        if (empty) {
            result = null;
        }

        try {
            Gson gson = new Gson();
            result = gson.fromJson(json, type);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            ChenLog.i("JsonToObject error:",json);
            result = null;
        }finally {
            return result;
        }


    }
}
