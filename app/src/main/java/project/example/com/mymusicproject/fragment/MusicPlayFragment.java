package project.example.com.mymusicproject.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.base.BaseFragment;
import project.example.com.mymusicproject.adapter.PlayPagerAdapter;
import project.example.com.mymusicproject.loader.PictureLoader;
import project.example.com.mymusicproject.model.MusicInfo;
import project.example.com.mymusicproject.net.DownLoadLrc;
import project.example.com.mymusicproject.util.FileUtils;
import project.example.com.mymusicproject.util.IndicatorLayout;
import project.example.com.mymusicproject.util.LrcView;
import project.example.com.mymusicproject.util.ScreenUtils;

/**
 * @auther ccc
 * created at 2016/7/4 9:40
 ***/

public class MusicPlayFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, SeekBar.OnSeekBarChangeListener {
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    /**
     * 返回按钮
     **/
    @Bind(R.id.iv_back)
    ImageView ivBack;
    /**
     * 歌曲名称
     **/
    @Bind(R.id.tv_title)
    TextView tvTitle;
    /**
     * 歌曲作者
     **/
    @Bind(R.id.tv_artist)
    TextView tvArtist;
    /**
     * 进度条
     **/
    @Bind(R.id.sb_progress)
    SeekBar sbProgress;
    /**
     * 播放时常
     **/
    @Bind(R.id.tv_current_time)
    TextView tvCurrentTime;
    /**
     * 总时长
     **/
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    /**
     * 播放按钮
     **/
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    /**
     * 下一曲
     **/
    @Bind(R.id.iv_next)
    ImageView ivNext;
    /**
     * 上一曲
     **/
    @Bind(R.id.iv_prev)
    ImageView ivPrev;
    /**
     * 封面图
     **/
    ImageView mAlbumCoverView;
    private int mLastProgress;
    @Bind(R.id.vp_play_page)
    ViewPager vpPlay;
    @Bind(R.id.il_indicator)
    IndicatorLayout ilIndicator;
    private List<View> mViewPagerContent;
    /**
     * 单行歌词
     **/
    private LrcView mLrcViewSingle;
    /**
     * 总歌词
     **/
    private LrcView mLrcViewFull;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    protected void init() {
        initSystemBar();
        initViewPager();
        ilIndicator.create(mViewPagerContent.size());
        onChange(getPlayService().getPlayingMusic());
    }

    /**
     * 初始化ViewPager
     **/
    private void initViewPager() {
        View coverView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_play_page_cover, null);//封面图
        View lrcView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_play_page_lrc, null);//歌词界面
        mAlbumCoverView = (ImageView) coverView.findViewById(R.id.iv_music_cover);
        mLrcViewSingle = (LrcView) coverView.findViewById(R.id.lrc_view_single);
        mLrcViewFull = (LrcView) lrcView.findViewById(R.id.lrc_view_full);
        mViewPagerContent = new ArrayList<>(2);
        mViewPagerContent.add(coverView);
        mViewPagerContent.add(lrcView);
        vpPlay.setAdapter(new PlayPagerAdapter(mViewPagerContent));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this);
    }

    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int top = ScreenUtils.getSystemBarHeight(getActivity());
            llContent.setPadding(0, top, 0, 0);
        }
    }

    /**
     * 更改播放的歌曲
     **/
    public void onChange(MusicInfo music) {
        onPlay(music);
    }

    /**
     * 播放和暂停
     **/
    public void onPlayerPause() {
        ivPlay.setSelected(false);
    }

    public void onPlayerResume() {
        ivPlay.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_play:
                play();
                break;
            case R.id.iv_next:
                next();
                break;
            case R.id.iv_prev:
                prev();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == sbProgress) {
            if (getPlayService().isPlaying() || getPlayService().isPause()) {
                int progress = seekBar.getProgress();
                getPlayService().seekTo(progress);
                tvCurrentTime.setText(formatTime(progress));
                mLastProgress = progress;
            } else {
                seekBar.setProgress(0);
            }
        }
    }

    /**
     * 底部栏 点击播放按钮
     **/
    private void onPlay(MusicInfo music) {
        if (music == null) {
            return;
        }
        setViewData(music);
        setLrc(music);
        sbProgress.setMax((int) music.getDuration());
        sbProgress.setProgress(0);
        mLastProgress = 0;
        tvCurrentTime.setText(R.string.play_time_start);
        tvTotalTime.setText(formatTime(music.getDuration()));
        if (getPlayService().isPlaying()) {
            ivPlay.setSelected(true);
        } else {
            ivPlay.setSelected(false);
        }
    }

    /**
     * 加载歌词
     **/
    private void setLrc(final MusicInfo music) {
        String lrcPath = FileUtils.getLrcFilePath(music);
        if (new File(lrcPath).exists()) {//判断歌词是否已经存在，存在的话直接加载否则去下载
            loadLrc(lrcPath);
        } else {
            new DownLoadLrc(music.getArtist(), music.getTitle()) {
                @Override
                public void onPrepare() {
                    mLrcViewSingle.searchLrc();
                    mLrcViewFull.searchLrc();
                    // 设置tag防止歌词下载完成后已切换歌曲
                    mLrcViewSingle.setTag(music);
                }

                @Override
                public void onFinish(@Nullable String lrcPath) {
                    if (mLrcViewSingle.getTag() == music) {
                        loadLrc(lrcPath);
                    }
                }
            }.execute();
        }
    }


    /**
     * 直接加载歌词
     *
     * @param path
     */
    private void loadLrc(String path) {
        mLrcViewSingle.loadLrc(path);
        mLrcViewFull.loadLrc(path);
        // 清除tag
        mLrcViewSingle.setTag(null);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        ilIndicator.setCurrent(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 更新View
     **/
    private void setViewData(MusicInfo music) {
        tvTitle.setText(music.getTitle());
        tvArtist.setText(music.getArtist());
        //1.是否载入默认 2.是否小图
        Bitmap cover = PictureLoader.getArtwork(getActivity(), music.getId(), music.getAlbumId(), true, false);
        if (cover == null) {
            mAlbumCoverView.setImageResource(R.drawable.play_page_default_cover);
        } else {
            mAlbumCoverView.setImageBitmap(cover);
        }
    }

    private void play() {
        getPlayService().playPause();
    }

    private void next() {
        getPlayService().next();
    }

    private void prev() {
        getPlayService().prev();
    }

    private void onBackPressed() {
        getActivity().onBackPressed();
        ivBack.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivBack.setEnabled(true);
            }
        }, 300);
    }

    private String formatTime(long time) {
        return FileUtils.formatTime("mm:ss", time);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 更新播放进度
     */
    public void onPublish(int progress) {
        sbProgress.setProgress(progress);
        //更新当前播放时间
        if (progress - mLastProgress >= 1000) {
            tvCurrentTime.setText(formatTime(progress));
            mLastProgress = progress;
            mLrcViewSingle.updateTime(progress);
            mLrcViewFull.updateTime(progress);
        }
    }
}
