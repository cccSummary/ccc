package project.example.com.mymusicproject;

import android.app.Application;
import android.content.res.Resources;

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
    }

    public static MusicApplication getInstance() {
        return sInstance;
    }

}
