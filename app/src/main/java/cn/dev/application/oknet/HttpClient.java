package cn.dev.application.oknet;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by air on 2015/12/26.
 */
public class HttpClient {

    public static final String TAG = "HttpClient";

    private static HttpClient httpClient;
    private static final OkHttpClient client = new OkHttpClient();

    public static HttpClient getInstance(){
        if (httpClient == null){
            synchronized (HttpClient.class){
                if (httpClient == null){
                    httpClient = new HttpClient();
                }
            }
        }
        return httpClient;
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/string; charset=utf-8");

    public void post() {
        String postBody = "ni mei a she me gui";

        Request request = new Request.Builder()
                .url("http://www.jinyuanbao.cn/")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();


        MediaType mediaType = request.body().contentType();
        String subtype = mediaType.subtype();
        Log.i(TAG,"type："+subtype);


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
//                Log.i(TAG,"onFailure"+reques);
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Log.i(TAG,"onResponse");

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.i(TAG,responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                Log.i(TAG,response.body().string());
            }
        });
    }
}
