package project.example.com.mymusicproject.page;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.base.BaseActivity;
import project.example.com.mymusicproject.dragListView.DragAdapter;
import project.example.com.mymusicproject.dragListView.DragListView;
import project.example.com.mymusicproject.fragment.MusicPlayFragment;
import project.example.com.mymusicproject.loader.MusicLoader;
import project.example.com.mymusicproject.loader.PictureLoader;
import project.example.com.mymusicproject.model.MusicInfo;
import project.example.com.mymusicproject.service.OnPlayerEventListener;
import project.example.com.mymusicproject.service.PlayService;
import project.example.com.mymusicproject.util.SystemUtils;

/**
 * Created by Administrator on 2016/6/21.
 * 音乐播放
 */
public class MusicListActivity extends BaseActivity implements ExpandableListView.OnChildClickListener,
        View.OnClickListener, OnPlayerEventListener {

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
     * 点击的音乐
     **/
    private int currentPosition;
    /**
     * 分组数据
     **/
    private List<String> group_list = new ArrayList<>();
    private Map<String, ArrayList<MusicInfo>> children;
    private MusicPlayFragment mPlayFragment;
    private PlayService mPlayService;
    private boolean isPlayFragmentShow = false;
    private MenuItem timerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);
        setGroupData();
        initView();
        setListener();
        bindService();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void parseIntent(Intent intent) {
        if (intent.hasExtra(Constants.FROM_NOTIFICATION)) {
            showPlayingFragment();
        }
    }

    private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((PlayService.PlayBinder) service).getService();
            mPlayService.setOnPlayEventListener(MusicListActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void play() {
        getPlayService().playPause();
    }

    private void previous() {
        getPlayService().prev();
    }

    private void next() {
        getPlayService().next();
    }

    public PlayService getPlayService() {
        return mPlayService;
    }

    /**
     * 初始化
     */
    public void initView() {
        expendlist.setDragOnLongPress(true);
        expendlist.setAdapter(new DragAdapter(this, group_list, children));
        int groupCount = expendlist.getCount();//默认列表展开
        for (int i = 0; i < groupCount; i++) {
            expendlist.expandGroup(i);
        }
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
                getPlayService().play(currentPosition);
                setViewData();
                break;
            case R.id.iv_play_bar_next://下一曲
                next();
                if (currentPosition < musicList.size()) {
                    currentPosition++;
                    setViewData();
                } else {
//                  Toast.
                }

                break;
            case R.id.iv_play_bar_previous://上一曲
                previous();
                if (currentPosition > 0) {
                    currentPosition--;
                    setViewData();
                } else {
//                    Toast.makeText("已经是第一首啦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fl_play_bar:
                showPlayingFragment();
                break;
        }
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

    public void onPlayerResume() {
        ivPlayBarPlay.setSelected(true);
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
        getPlayService().play(currentPosition);
        setViewData();
        return false;
    }

    private void setViewData() {
        MusicInfo music = musicList.get(currentPosition);
        tvPlayBarTitle.setText(music.getTitle());
        tvPlayBarArtist.setText(music.getArtist());
        Bitmap cover = PictureLoader.getArtwork(this, music.getId(), music.getAlbumId(), true, false);
        if (cover == null) {
            ivPlayBarCover.setImageResource(R.drawable.default_cover);
        } else {
            ivPlayBarCover.setImageBitmap(cover);
        }
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

    @Override
    public void onTimer(long remain) {
        if (timerItem == null) {
        }
        String title = getString(R.string.menu_timer);
        timerItem.setTitle(remain == 0 ? title : SystemUtils.formatTime(title + "(mm:ss)", remain));
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
}
