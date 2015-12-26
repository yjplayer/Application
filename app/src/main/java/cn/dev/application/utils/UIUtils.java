package cn.dev.application.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import cn.dev.application.DefaultApplication;

/**
 * Created by air on 15/12/25.
 */
public class UIUtils {

    /**
     * 获取全局的Context
     * @return
     */
    public static Context getContext(){
        return DefaultApplication.context;
    }

    /**
     * dip转换为px
     * @param value
     * @return
     */
    public static int dip2px(float value){
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return (int) (value*displayMetrics.density + 0.5f);
    }

    /**
     * px转换为dip
     * @param value
     * @return
     */
    public static int px2dip(float value){
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return (int) (value/displayMetrics.density+0.5f);
    }

    /**
     * 获取屏幕的width
     * @return
     */
    public static int getScreenWidth(){
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕的height
     * @return
     */
    public static int getScreenHeight(){
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

}
