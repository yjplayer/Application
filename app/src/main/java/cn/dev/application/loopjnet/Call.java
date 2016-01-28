package cn.dev.application.loopjnet;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cn.dev.application.utils.XLog;

/**
 * Created by air on 16/1/28.
 */
public class Call {

    private Request request;
    private AsyncHttpClient asyncHttpClient;

    public Call(Request request,AsyncHttpClient asyncHttpClient) {
        this.request = request;
        this.asyncHttpClient = asyncHttpClient;
    }

    public void execute(final Callback callback){

        String url = request.url();

        TextHttpResponseHandler textHttpResponseHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                XLog.i(statusCode + "\t" + (responseString == null ? "response is null" : responseString));
                callback.onFailure(responseString);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                try {
                    //TODO parse code
                    XLog.i("onSuccess\n" + responseString == null ? "response is null" : responseString);
                    callback.onSuccess(responseString);
                } catch (Exception e) {
                    XLog.e("catch a exception", e);
                }
            }
        };

        XLog.i("===========================================http ===========================================");
        XLog.i(url);
        asyncHttpClient.post(url, request.body().getRequestParams(), textHttpResponseHandler);
        XLog.i("===========================================http ===========================================");
    }
}
