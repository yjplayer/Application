package cn.dev.application.loopjnet;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

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

    public Call newCall(Request request){
        return new Call(request,asyncHttpClient);
    }

    public void load(String url,FileAsyncHttpResponseHandler handler){
        asyncHttpClient.get(url, handler);
    }
}
