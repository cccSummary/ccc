package project.example.com.mymusicproject.page;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.adapter.FragmentAdapter;
import project.example.com.mymusicproject.base.BaseActivity;
import project.example.com.mymusicproject.fragment.LocalMusicFragment;
import project.example.com.mymusicproject.fragment.MusicPlayFragment;
import project.example.com.mymusicproject.fragment.OnlineMusicListFragment;
import project.example.com.mymusicproject.loader.PictureLoader;
import project.example.com.mymusicproject.model.MusicInfo;
import project.example.com.mymusicproject.service.OnPlayerEventListener;
import project.example.com.mymusicproject.service.PlayService;
import project.example.com.mymusicproject.util.FileUtils;

/**
 * Created by Administrator on 2016/7/7.
 */
public class MainMusicActivity extends BaseActivity implements View.OnClickListener, OnPlayerEventListener, ViewPager.OnPageChangeListener {

    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.iv_play_bar_cover)
    ImageView ivPlayBarCover;
    @Bind(R.id.tv_play_bar_title)
    TextView tvPlayBarTitle;
    @Bind(R.id.tv_play_bar_artist)
    TextView tvPlayBarArtist;
    @Bind(R.id.iv_play_bar_previous)
    ImageView ivPlayBarPrevious;
    @Bind(R.id.iv_play_bar_play)
    ImageView ivPlayBarPlay;
    @Bind(R.id.iv_play_bar_next)
    ImageView ivPlayBarNext;
    @Bind(R.id.fl_play_bar)
    LinearLayout flPlayBar;
    private MusicPlayFragment mPlayFragment;
    private PlayService mPlayService;
    private boolean isPlayFragmentShow = false;
    /**
     * 点击的音乐
     **/
    private int currentPosition;
    /***
     * 音乐数据集
     **/
    private ArrayList<MusicInfo> musicList;
    private MenuItem timerItem;
    public LocalMusicFragment mLocalMusicFragment;
    public OnlineMusicListFragment mSongListFragment;
    @Bind(R.id.tv_local_music)
    TextView tvLocalMusic;
    @Bind(R.id.tv_online_music)
    TextView tvOnlineMusic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_include);
        ButterKnife.bind(this);
        bindService();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    @Override
    protected void onDestroy() {
        unbindService(mPlayServiceConnection);
        super.onDestroy();
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
            mPlayService.setOnPlayEventListener(MainMusicActivity.this);
            init();
            parseIntent(getIntent());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void init() {
        setupView();
        onChange(mPlayService.getPlayingMusic());
    }

    @Override
    protected void setListener() {
        tvLocalMusic.setOnClickListener(this);
        tvOnlineMusic.setOnClickListener(this);
        viewpager.setOnPageChangeListener(this);
        flPlayBar.setOnClickListener(this);
        ivPlayBarPlay.setOnClickListener(this);
        ivPlayBarNext.setOnClickListener(this);
    }

    private void setupView() {
        // setup view pager
        mLocalMusicFragment = new LocalMusicFragment();
        mSongListFragment = new OnlineMusicListFragment();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(mLocalMusicFragment);
        adapter.addFragment(mSongListFragment);
        viewpager.setAdapter(adapter);
        tvLocalMusic.setSelected(true);
    }

    private void parseIntent(Intent intent) {
        if (intent.hasExtra(Constants.FROM_NOTIFICATION)) {
            showPlayingFragment();
        }
    }

    /**
     * 更新播放进度
     */
    @Override
    public void onPublish(int progress) {
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPublish(progress);
        }
    }

    @Override
    public void onChange(MusicInfo music) {
        onPlay(music);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onChange(music);
        }
    }

    @Override
    public void onPlayerPause() {
        ivPlayBarPlay.setSelected(false);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlayerPause();
        }
    }

    @Override
    public void onPlayerResume() {
        ivPlayBarPlay.setSelected(true);
        if (mPlayFragment != null && mPlayFragment.isResume()) {
            mPlayFragment.onPlayerResume();
        }
    }

    @Override
    public void onTimer(long remain) {
        if (timerItem == null) {
//            timerItem = navigationView.getMenu().findItem(R.id.action_timer);
        }
        String title = getString(R.string.menu_timer);
        timerItem.setTitle(remain == 0 ? title : FileUtils.formatTime(title + "(mm:ss)", remain));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_menu:
//                drawerLayout.openDrawer(GravityCompat.START);
//                break;
//            case R.id.iv_search:
//                startActivity(new Intent(this, SearchMusicActivity.class));
//                break;
            case R.id.tv_local_music:
                viewpager.setCurrentItem(0);
                break;
            case R.id.tv_online_music:
                viewpager.setCurrentItem(1);
                break;
            case R.id.fl_play_bar:
                showPlayingFragment();
                break;
            case R.id.iv_play_bar_play:
                play();
                break;
            case R.id.iv_play_bar_next:
                next();
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            tvLocalMusic.setSelected(true);
            tvOnlineMusic.setSelected(false);
        } else {
            tvLocalMusic.setSelected(false);
            tvOnlineMusic.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void onPlay(MusicInfo music) {
        if (music == null) {
            return;
        }
        Bitmap cover = PictureLoader.getArtwork(this, music.getId(), music.getAlbumId(), true, false);
        if (cover == null) {
            ivPlayBarCover.setImageResource(R.drawable.default_cover);
        } else {
            ivPlayBarCover.setImageBitmap(cover);
        }
        tvPlayBarTitle.setText(music.getTitle());
        tvPlayBarArtist.setText(music.getArtist());
        if (getPlayService().isPlaying()) {
            ivPlayBarPlay.setSelected(true);
        } else {
            ivPlayBarPlay.setSelected(false);
        }
        if (mLocalMusicFragment != null && mLocalMusicFragment.isResume()) {
//            mLocalMusicFragment.onItemPlay();
        }
    }

    private void play() {
        getPlayService().playPause();
    }

    private void next() {
        getPlayService().next();
    }

    public PlayService getPlayService() {
        return mPlayService;
    }

    private void showPlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new MusicPlayFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commit();
        isPlayFragmentShow = true;
    }

    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commit();
        isPlayFragmentShow = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 切换夜间模式不保存状态
    }

    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        if (Boolean.parseBoolean("true")) {
            super.onBackPressed();
        } else {
            moveTaskToBack(false);
        }
    }
}
