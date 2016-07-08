package project.example.com.mymusicproject.net;

import android.support.annotation.Nullable;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import okhttp3.Call;
import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.callback.JsonCallback;
import project.example.com.mymusicproject.model.JOnlineMusic;
import project.example.com.mymusicproject.model.JOnlineMusicList;

/**
 * Created by Administrator on 2016/7/7.
 */
public abstract class MusicListLoad {
    private String type;

    public MusicListLoad(String type) {
        this.type = type;

    }

    public void execute() {
        onPrepare();
        getMusicList();
    }

    public void getMusicList() {
        OkHttpUtils.get().url(Constants.BASE_URL).
                addParams(Constants.PARAM_METHOD, Constants.METHOD_GET_MUSIC_LIST).
                addParams(Constants.PARAM_TYPE, "1")
                .addParams(Constants.PARAM_OFFSET, "0").
                addParams(Constants.PARAM_SIZE, "10").build().
                execute(new JsonCallback<JOnlineMusicList>(JOnlineMusicList.class) {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(JOnlineMusicList response) {
                if (response != null) {
                    onFinish(response.getSong_list());
                }

            }
        });
    }


    public abstract void onPrepare();

    public abstract void onFinish(@Nullable List<JOnlineMusic> onlineMusics);
}
