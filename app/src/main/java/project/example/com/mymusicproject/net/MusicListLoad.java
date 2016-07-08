package project.example.com.mymusicproject.net;

import android.support.annotation.Nullable;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import okhttp3.Call;
import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.callback.JsonCallback;
import project.example.com.mymusicproject.model.DownMusicInfo;
import project.example.com.mymusicproject.model.JOnlineMusic;
import project.example.com.mymusicproject.model.JOnlineMusicList;

/**
 * Created by Administrator on 2016/7/7.
 */
public abstract class MusicListLoad {
    private String type = "1";
    private JOnlineMusic music;

    public MusicListLoad(String type, JOnlineMusic music) {
        this.type = type;
        this.music = music;
    }

    public void execute() {
        onPrepare();
        getMusicList();
    }

    public void getMusicList() {
        OkHttpUtils.get().url(Constants.BASE_URL).
                addParams(Constants.PARAM_METHOD, Constants.METHOD_GET_MUSIC_LIST).
                addParams(Constants.PARAM_TYPE, type)
                .addParams(Constants.PARAM_OFFSET, "0").
                addParams(Constants.PARAM_SIZE, "10").build().
                execute(new JsonCallback<JOnlineMusicList>(JOnlineMusicList.class) {
                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(JOnlineMusicList response) {
                        if (response != null) {
                            onFinish(response.getSong_list(), null);
                        }

                    }
                });
    }

    public void playMusic() {
        playMusicMed();
    }

    /***
     * 播放音乐
     */
    public void playMusicMed() {
        OkHttpUtils.get().url(Constants.BASE_URL).
                addParams(Constants.PARAM_METHOD, Constants.METHOD_DOWNLOAD_MUSIC).
                addParams(Constants.PARAM_SONG_ID, music.getSong_id())
                .build().
                execute(new JsonCallback<DownMusicInfo>(DownMusicInfo.class) {
                    @Override
                    public void onError(Call call, Exception e) {
                    }

                    @Override
                    public void onResponse(DownMusicInfo response) {
                        if (response != null) {
                            onFinish(null, response.getBitrate());
                        }

                    }
                });
    }

    public abstract void onPrepare();

    public abstract void onFinish(@Nullable List<JOnlineMusic> musicList, @Nullable DownMusicInfo.BitrateBean bitrate);
}
