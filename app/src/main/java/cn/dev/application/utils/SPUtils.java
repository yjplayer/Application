package cn.dev.application.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;
import java.util.Set;

/**
 * App的SharedPreferences存储读取
 * Created by air on 15/12/26.
 */
public class SPUtils {

    public static final String TAG = "SharedPreferences";

    /**
     * /data/data/packageName/APP_LOCAL_INFO.xml
     */
    public static final String APP_LOCAL_INFO = "app_local_info";

    public static SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences = UIUtils.getContext().getSharedPreferences(
                APP_LOCAL_INFO, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    /**
     * 在SharedPreferences中存储String类型
     *
     * @param key
     * @param value
     */
    public static void saveString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }

    /**
     * see {@link #saveString(String, String)}
     */
    public static void saveInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();
    }

    /**
     * see {@link #saveString(String, String)}
     */
    public static void saveBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();
    }

    /**
     * 通过key获取本地存储的value，没有返回默认的defValue
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    /**
     * see {@link #getString(String, String)}
     */
    public static int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    /**
     * see {@link #getString(String, String)}
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    /**
     * 移除key及对应的value
     *
     * @param key
     */
    public static void remove(String key) {
        getSharedPreferences().edit().remove(key).commit();
    }

    /**
     * 打印SharedPreferences存储的所有信息。
     */
    public static void logSP() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Map<String, ?> map = sharedPreferences.getAll();

        Log.i(TAG, "===========================================sharedPreferences===========================================");

        if (map.size() == 0) {
            Log.i(TAG, "sharedPreferences is null");
        } else {
            Set<? extends Map.Entry<String, ?>> entries = map.entrySet();
            for (Map.Entry<String, ?> entry : entries) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Log.i(TAG, key + "\t\t\t\t\t" + value);
            }
        }

        Log.i(TAG, "===========================================sharedPreferences===========================================");
    }
}
