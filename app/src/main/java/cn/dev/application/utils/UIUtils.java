package cn.dev.application.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cn.dev.application.DefaultApplication;

/**
 * Created by air on 15/12/25.
 */
public class UIUtils {

    /**
     * 获取全局的Context
     *
     * @return
     */
    public static Context getContext() {
        return DefaultApplication.context;
    }

    /**
     * 显示键盘，可以在Activity的android:windowSoftInputMode节点设置属性来实现
     * @param view
     */
    public static void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 隐藏键盘
     * @param view
     */
    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * dip转换为px
     *
     * @param value
     * @return
     */
    public static int dip2px(float value) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return (int) (value * displayMetrics.density + 0.5f);
    }

    /**
     * px转换为dip
     *
     * @param value
     * @return
     */
    public static int px2dip(float value) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return (int) (value / displayMetrics.density + 0.5f);
    }

    /**
     * 获取屏幕的width
     *
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕的height
     *
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

}
