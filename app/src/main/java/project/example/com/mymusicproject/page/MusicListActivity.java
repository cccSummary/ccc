package project.example.com.mymusicproject.page;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.dragListView.DragAdapter;
import project.example.com.mymusicproject.dragListView.DragListView;
import project.example.com.mymusicproject.loader.MusicLoader;
import project.example.com.mymusicproject.loader.PictureLoader;
import project.example.com.mymusicproject.model.MusicInfo;

/**
 * Created by Administrator on 2016/6/21.
 * 音乐播放
 */
public class MusicListActivity extends Activity implements ExpandableListView.OnChildClickListener, View.OnClickListener {

    @Bind(R.id.expendlist)
    DragListView expendlist;
    /**
     * 暂停or播放
     **/
    @Bind(R.id.iv_play_bar_play)
    ImageView ivPlayBarPlay;
    /**
     * 下一曲
     **/
    @Bind(R.id.iv_play_bar_next)
    ImageView ivPlayBarNext;
    /**
     * 上一曲
     **/
    @Bind(R.id.iv_play_bar_previous)
    ImageView ivPlayBarPreious;
    @Bind(R.id.fl_play_bar)
    LinearLayout flPlayBar;
    /**
     * 标题
     **/
    @Bind(R.id.base_titlebar_title)
    TextView baseTitlebarTitle;
    /**
     * 封面图
     **/
    @Bind(R.id.iv_play_bar_cover)
    ImageView ivPlayBarCover;
    /**
     * 歌曲名称
     **/
    @Bind(R.id.tv_play_bar_title)
    TextView tvPlayBarTitle;
    /**
     * 作者
     **/
    @Bind(R.id.tv_play_bar_artist)
    TextView tvPlayBarArtist;
    /***
     * 音乐数据集
     **/
    private ArrayList<MusicInfo> musicList;
    /**
     * 音频播放控件
     **/
    private MediaPlayer mediaPlayer;
    /**
     * 点击的音乐
     **/
    private int currentPosition;
    /**
     * 分组数据
     **/
    private List<String> group_list = new ArrayList<>();
    private Map<String, ArrayList<MusicInfo>> children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);
        setGroupData();
        initView();
        setListener();
    }

    /**
     * 初始化
     */
    public void initView() {
        mediaPlayer = new MediaPlayer();
        expendlist.setDragOnLongPress(true);
        expendlist.setAdapter(new DragAdapter(this, group_list, children));
        int groupCount = expendlist.getCount();//默认列表展开
        for (int i = 0; i < groupCount; i++) {
            expendlist.expandGroup(i);
        }
        baseTitlebarTitle.setText("音乐列表");
        setViewData();
    }

    /***
     * 点击事件
     */
    public void setListener() {
        expendlist.setOnChildClickListener(this);
        ivPlayBarNext.setOnClickListener(this);
        ivPlayBarPlay.setOnClickListener(this);
        ivPlayBarPreious.setOnClickListener(this);
        flPlayBar.setOnClickListener(this);
    }

    /**
     * 设置数据
     */
    public void setGroupData() {
        group_list.add("列表一");
        group_list.add("列表二");
        MusicLoader musicLoader = MusicLoader.instance(getContentResolver());//获取音乐
        musicList = (ArrayList<MusicInfo>) musicLoader.getMusicList();
        setChildrenData();
    }

    private void setChildrenData() {
        children = Collections.synchronizedMap(new LinkedHashMap<String, ArrayList<MusicInfo>>());

        for (String s : group_list) {
            ArrayList<MusicInfo> array = new ArrayList<MusicInfo>();
            array.addAll(musicList);
            children.put(s, array);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_play_bar_play:
                //当音乐在播放时
                if (mediaPlayer.isPlaying()) {
                    //暂停
                    pause();
                } else {
                    //不在播放，则播放
                    setMediaPlay();
                }
                break;
            case R.id.iv_play_bar_next://下一曲
                next();
                break;
            case R.id.iv_play_bar_previous://上一曲
                previous();
                break;
            case R.id.fl_play_bar:
                gotoMusicDetail();
                break;
        }
    }

    //播放上一曲
    private void previous() {
        //判断是否为第一首歌曲，若为第一首歌曲，则播放最后一首
        currentPosition--;
        if (currentPosition <= 0) {
            Toast.makeText(this, "已经是第一首了", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //播放
            setMediaPlay();
        }
    }

    //播放下一曲（与上一曲类似）
    private void next() {
        currentPosition++;
        if (currentPosition >= musicList.size()) {
            Toast.makeText(this, "已经是最后一首了", Toast.LENGTH_SHORT).show();
            return;
        } else {
            setMediaPlay();
        }
    }


    //暂停
    private void pause() {
        //直接调用MediaPlay 中的暂停方法
        mediaPlayer.pause();
        //切换为播放的按钮（按钮为android系统自带的按钮，可直接用）
        ivPlayBarPlay.setSelected(true);
    }

    /***
     * 设置播放
     *
     * @param
     */
    public void setMediaPlay() {
        //重置
        mediaPlayer.reset();
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置音乐文件来源
            String path = musicList.get(currentPosition).getUrl();
            mediaPlayer.setDataSource(path);
            //准备（缓冲文件）
            mediaPlayer.prepare();
            //播放开始
            mediaPlayer.start();
            //设置按钮图片为暂停图标
            ivPlayBarPlay.setSelected(false);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    next();//顺序播放
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 子类目的点击事件
     *
     * @param expandableListView
     * @param view
     * @param i
     * @param i1
     * @param l
     * @return
     */
    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
        currentPosition = i1;
        setMediaPlay();
        setViewData();

        return false;
    }

    private void setViewData() {
        MusicInfo music = musicList.get(currentPosition);
        //1.是否载入默认 2.是否小图
        Bitmap bm = PictureLoader.getArtwork(this, music.getId(), music.getAlbumId(), true, false);
        if (bm == null) {
            ivPlayBarCover.setImageResource(R.drawable.default_cover);
        } else {
            ivPlayBarCover.setImageBitmap(bm);
        }
        tvPlayBarTitle.setText(music.getTitle());
        tvPlayBarArtist.setText(music.getArtist());

    }

    /*
    *去到音乐播放的详情界面
     */
    public void gotoMusicDetail() {
        startActivity(new Intent(MusicDetailActivity.getIntent(this, musicList.get(currentPosition))));
    }
}
