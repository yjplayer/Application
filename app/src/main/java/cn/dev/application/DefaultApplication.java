package cn.dev.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by air on 15/12/25.
 */
public class DefaultApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
    }
}
