package cn.dev.application.loopjnet;

import com.google.gson.Gson;

/**
 * Created by air on 2015/12/26.
 */
public abstract class Callback {

    public abstract void onFailure(String errorDes);

    public abstract void onSuccess(String responseString);

    public <T> T gsonParse(String content,Class<T> clazz){
        Gson gson = new Gson();
        T t = gson.fromJson(content, clazz);
        return t;
    }
}
