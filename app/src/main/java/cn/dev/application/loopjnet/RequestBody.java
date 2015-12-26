package cn.dev.application.loopjnet;

import android.util.Log;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by air on 2015/12/26.
 */
public class RequestBody {

    public boolean encrypt;
    public JSONObject jsonObject;
    public RequestParams requestParams;

    public RequestBody(JSONObject jsonObject){
        encrypt = true;
        this.jsonObject = jsonObject!=null?jsonObject:new JSONObject();
        this.requestParams = new RequestParams();
        addBaseRequestDataEncrype();
    }

    public RequestBody(RequestParams params){
        encrypt = false;
        this.requestParams = params!=null?params:new RequestParams();
        addBaseRequestDataUnencrype();
    }

    private void addBaseRequestDataEncrype() {
        try {
            addBaseRequestDataUnencrype();
            jsonObject.put("client","android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBaseRequestDataUnencrype() {
        requestParams.put("client","android");
    }

    public RequestParams createRequestParams() {
        if (encrypt){
            Log.i(HttpClient.TAG,"待加密数据："+jsonObject.toString());
            requestParams.put("data",encryptData());
        }
        Log.i(HttpClient.TAG,"请求参数："+requestParams.toString());
        return requestParams;
    }

    public String encryptData(){
        try {
            jsonObject.put("encrypt","ok");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
