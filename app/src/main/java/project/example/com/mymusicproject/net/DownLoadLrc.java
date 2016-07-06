package project.example.com.mymusicproject.net;


import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;
import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.callback.JsonCallback;
import project.example.com.mymusicproject.model.LrcData;
import project.example.com.mymusicproject.model.SearchMusic;
import project.example.com.mymusicproject.util.FileUtils;

/**
 * 如果本地歌曲没有歌词则从网络搜索歌词
 */
public abstract class DownLoadLrc {
    private String artist;
    private String title;

    private long musicId;

    public DownLoadLrc(String artist, String title) {
        this.artist = artist;
        this.title = title;
        this.musicId = musicId;
    }

    public void execute() {
        onPrepare();
        searchLrc();
    }

    /**
     * 先搜索歌曲，从而获得songid，用的是百度音乐api
     **/
    private void searchLrc() {
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_SEARCH_MUSIC)
                .addParams(Constants.PARAM_QUERY, title)
                .build()
                .execute(new JsonCallback<SearchMusic>(SearchMusic.class) {//此接口返回的是一个列表，所以取第一个歌的id
                    @Override
                    public void onResponse(SearchMusic response) {
                        if (response == null || response.getSong() == null) {
                            onFinish(null);
                            return;
                        }
                        String songID = response.getSong().get(0).getSongid();//
                        downloadLrc(songID);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        onFinish(null);
                    }
                });
    }

    /**
     * 得到songid后 然后去请求得到歌词
     **/
    private void downloadLrc(String songId) {
        OkHttpUtils.get().url(Constants.BASE_URL)
                .addParams(Constants.PARAM_METHOD, Constants.METHOD_LRC)
                .addParams(Constants.PARAM_SONG_ID, songId)
                .build()
                .execute(new JsonCallback<LrcData>(LrcData.class) {
                    @Override
                    public void onResponse(LrcData response) {
                        if (response == null || TextUtils.isEmpty(response.getLrcContent())) {
                            onFinish(null);
                            return;
                        }
                        String lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(artist, title);//在本地创建一个目录
                        FileUtils.saveLrcFile(lrcPath, response.getLrcContent());//保存获取到的歌词,防止再次下载
                        onFinish(lrcPath);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        onFinish(null);
                    }
                });
    }

    public abstract void onPrepare();

    public abstract void onFinish(@Nullable String lrcPath);
}
