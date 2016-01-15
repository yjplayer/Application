package cn.dev.application.loopjnet;

import android.text.TextUtils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by air on 2015/12/26.
 */
public class HttpClient {

    public static final String TAG = "HttpClient";

    private static HttpClient httpClient ;
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static HttpClient getInstance(){
        if (httpClient == null){
            synchronized (HttpClient.class){
                if (httpClient == null){
                    httpClient = new HttpClient();
                    initializeNet();
                }
            }
        }
        return httpClient;
    }

    /**
     * 设置超时时间，UA……
     */
    private static void initializeNet() {
        asyncHttpClient.setTimeout(1000*30);
        asyncHttpClient.setUserAgent("client_info:Android");
    }

    public void post(String relativeUrl, final RequestBody requestBody, final Callback callback){
        String url = !TextUtils.isEmpty(relativeUrl)&&relativeUrl.startsWith("http://")?
                relativeUrl: API.BASE_URL+relativeUrl;

        TextHttpResponseHandler textHttpResponseHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG,statusCode+"\t"+(responseString==null?"response is null":responseString));
                callback.onFailure(responseString);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                try {
                    //TODO parse code
                    Log.i(TAG,"onSuccess\n"+responseString==null?"response is null":responseString);
                    callback.onSuccess(responseString);
                } catch (Exception e) {
                    Log.i(TAG,"catch a exception",e);
                }
            }
        };

        Log.i(TAG,"===========================================http ===========================================");
        Log.i(TAG,relativeUrl==null? API.BASE_URL:relativeUrl);
        asyncHttpClient.post(url,requestBody.createRequestParams(),textHttpResponseHandler);
        Log.i(TAG,"===========================================http ===========================================");
    }

    public void load(String url,FileAsyncHttpResponseHandler handler){
        asyncHttpClient.get(url, handler);
    }
}
