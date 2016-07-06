package project.example.com.mymusicproject;

import android.app.Application;
import android.content.res.Resources;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import project.example.com.mymusicproject.util.Preferences;

/**
 * @auther ccc
 * created at 2016/7/1 9:49
 ***/

public class MusicApplication extends Application {
    private static MusicApplication sInstance;
    private static Resources sRes;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sRes = getResources();
        Preferences.init(this);
        initOkHttpUtils();
    }

    public static MusicApplication getInstance() {
        return sInstance;
    }

    /***
     * 配置okhtpp
     */
    private void initOkHttpUtils() {
        OkHttpUtils.getInstance().setConnectTimeout(30, TimeUnit.SECONDS);
        OkHttpUtils.getInstance().setReadTimeout(30, TimeUnit.SECONDS);
        OkHttpUtils.getInstance().setWriteTimeout(30, TimeUnit.SECONDS);
    }
}
