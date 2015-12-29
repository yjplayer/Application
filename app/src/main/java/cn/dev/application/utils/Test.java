package cn.dev.application.utils;

import android.util.Log;

import cn.dev.application.BuildConfig;

/**
 * Created by air on 2015/12/28.
 */
public class Test {

    public static void test(){

        Log.i("TAG", BuildConfig.LOG_DEBUG+"\t"+BuildConfig.BASE_URL);
    }
}
