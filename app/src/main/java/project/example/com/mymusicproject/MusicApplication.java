package project.example.com.mymusicproject;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.util.LongSparseArray;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import project.example.com.mymusicproject.util.Preferences;

/**
 * @auther ccc
 * created at 2016/7/1 9:49
 ***/

public class MusicApplication extends Application {
    private static MusicApplication sInstance;
    private LongSparseArray<String> mDownloadList = new LongSparseArray<>();
    private static Resources sRes;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sRes = getResources();
        Preferences.init(this);
        updateNightMode(Preferences.isNightMode());;
        initImageLoader();
    }

    public static MusicApplication getInstance() {
        return sInstance;
    }

    public LongSparseArray<String> getDownloadList() {
        return mDownloadList;
    }

    private void initImageLoader() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(2 * 1024 * 1024) // 2MB
                .diskCacheSize(50 * 1024 * 1024) // 50MB
                .build();
        ImageLoader.getInstance().init(configuration);
    }

    public static void updateNightMode(boolean on) {
        DisplayMetrics dm = sRes.getDisplayMetrics();
        Configuration config = sRes.getConfiguration();
        config.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        config.uiMode |= on ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        sRes.updateConfiguration(config, dm);
    }
}
