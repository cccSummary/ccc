package project.example.com.mymusicproject.page;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.adapter.BaseRecyclerAdapter;
import project.example.com.mymusicproject.adapter.OnlineMusicAdapter;
import project.example.com.mymusicproject.base.BaseActivity;
import project.example.com.mymusicproject.model.DownMusicInfo;
import project.example.com.mymusicproject.model.JOnlineMusic;
import project.example.com.mymusicproject.model.MusicInfo;
import project.example.com.mymusicproject.model.SongListInfo;
import project.example.com.mymusicproject.net.MusicListLoad;
import project.example.com.mymusicproject.service.PlayService;

/**
 * Created by Administrator on 2016/7/7.
 * 在线音乐列表分类
 */
public class OnLineMusicActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.recyclerView)
    RecyclerView mRecycleview;
    OnlineMusicAdapter mAdapter;
    private SongListInfo mListInfo;
    List<JOnlineMusic> jOnlineMusics = new ArrayList<>();
    private PlayService mPlayService;
    @Bind(R.id.tv_local_music)
    TextView mTitleText;
    @Bind(R.id.iv_menu)
    ImageView mReturnIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music_layout);
        mListInfo = (SongListInfo) getIntent().getSerializableExtra(Constants.PARAM_DATA);
        setTitle("dsgfdghg");
        bindService();
        initView();
    }

    private void initView() {

        // 线性布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // 设置布局管理器
        mRecycleview.setLayoutManager(linearLayoutManager);
        mAdapter = new OnlineMusicAdapter(this);
        mRecycleview.setAdapter(mAdapter);
        initData();
        mAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object... params) {
                JOnlineMusic music = mAdapter.getItem(position);
                playMusic(music);

            }
        });
        mReturnIcon.setVisibility(View.VISIBLE);
        mTitleText.setText(mListInfo.getTitle());
    }

    @Override
    public void onClick(View view) {
        if (view == mReturnIcon) {
            finish();
        }
    }

    public void playMusic(JOnlineMusic music) {
        final MusicInfo musicInfo = new MusicInfo();
        new MusicListLoad("", music) {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onFinish(@Nullable List<JOnlineMusic> onlineMusics, DownMusicInfo.BitrateBean bitrate) {
                musicInfo.setUri(bitrate.getFile_link());
                musicInfo.setDuration(bitrate.getFile_duration() * 1000);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mPlayService.play(musicInfo);
                    }
                }).start();
            }
        }.playMusic();

    }

    private void initData() {
        new MusicListLoad(mListInfo.getType(), null) {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onFinish(@Nullable List<JOnlineMusic> onlineMusics, DownMusicInfo.BitrateBean bitrate) {
                jOnlineMusics.addAll(onlineMusics);
                mAdapter.setData(onlineMusics);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    protected void setListener() {

    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((PlayService.PlayBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


}
