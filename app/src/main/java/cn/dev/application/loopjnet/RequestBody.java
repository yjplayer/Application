package cn.dev.application.loopjnet;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.dev.application.utils.AppUtils;
import cn.dev.application.utils.XLog;

/**
 * Created by air on 16/1/28.
 */
public class RequestBody {

    public RequestBody() {
        requestParams = new RequestParams();
        map = new HashMap<>();
        addDefaultParams();
    }


    private RequestParams requestParams;
    private boolean encrypt;
    private Map<String, Object> map;

    public void encrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public RequestBody addParams(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public RequestParams getRequestParams() {
        JSONObject jsonObject = null;
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entrySet : entries) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if (encrypt) {
                if (jsonObject == null) {
                    jsonObject = new JSONObject();
                }
                if (value instanceof String[]) {
                    String[] strings = (String[]) value;
                    JSONArray jsonArray = new JSONArray();
                    for (String s : strings) {
                        jsonArray.put(s);
                    }
                    try {
                        XLog.i(jsonArray.toString());
                        jsonObject.put(key, jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject.put(key, value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                requestParams.put(key, value);
            }
        }
        XLog.logJson(jsonObject.toString());
        if (jsonObject != null) {
            requestParams.put("data", "encryptData");
        }
        requestParams.put("version", AppUtils.getVersionName());
        requestParams.put("client", "android");
        XLog.i(requestParams.toString());
        return requestParams;
    }

    private void addDefaultParams() {
        map.put("version", AppUtils.getVersionName());
        map.put("client", "android");
    }
}
