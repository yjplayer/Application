package cn.dev.application.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by air on 15/12/28.
 */
public class AppUtils {

    /**
     * 获取versionName
     * @return
     */
    public String getVersionName(){
        Context context = UIUtils.getContext();
        String versioname = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versioname = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versioname;
    }

    /**
     * 获取versionCode
     * @return
     */
    public int getVersionCode(){
        Context context = UIUtils.getContext();
        int versionCode = 0;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
