package project.example.com.mymusicproject.net;

import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;
import project.example.com.mymusicproject.callback.JsonCallback;
import project.example.com.mymusicproject.model.MusicData;

/**
 * Created by Administrator on 2016/7/8.
 */
public class Request {

    public Request() {

    }

    public void execute() {
        getRequest();
//        onPrepare();
//        searchLrc();
    }

    public void getRequest() {
        OkHttpUtils.get().url("https://suggest.yandex.ru/suggest-ya.cgi?callback=jQuery21407720381293953593_1467941858448&srv=morda_ru_desktop&wiz=TrWth&lr=21431&uil=ru&fact=1&v=4&icon=1&hl=1&html=1&bemjson=1&yu=344254941467857979&pos=1&part=1&_=1467941858453").
                build().execute(new JsonCallback<MusicData>(MusicData.class) {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(MusicData response) {

            }
        });
    }
}
