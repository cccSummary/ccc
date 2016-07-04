package project.example.com.mymusicproject.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.base.BaseFragment;
import project.example.com.mymusicproject.enums.PlayModeEnum;
import project.example.com.mymusicproject.loader.PictureLoader;
import project.example.com.mymusicproject.model.MusicInfo;
import project.example.com.mymusicproject.util.Preferences;
import project.example.com.mymusicproject.util.ScreenUtils;
import project.example.com.mymusicproject.util.SystemUtils;

/**
 * @auther ccc
 * created at 2016/7/4 9:40
 ***/

public class MusicPlayFragment extends BaseFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_artist)
    TextView tvArtist;
    @Bind(R.id.sb_progress)
    SeekBar sbProgress;
    @Bind(R.id.tv_current_time)
    TextView tvCurrentTime;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    //    @Bind(R.id.iv_mode)
//    ImageView ivMode;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.iv_next)
    ImageView ivNext;
    @Bind(R.id.iv_prev)
    ImageView ivPrev;
    @Bind(R.id.iv_music_cover)
    ImageView mAlbumCoverView;
    private int mLastProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    protected void init() {
        initSystemBar();
        initPlayMode();
        onChange(getPlayService().getPlayingMusic());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(this);
//        ivMode.setOnClickListener(this);
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

    private void initPlayMode() {
        int mode = Preferences.getPlayMode();
//        ivMode.setImageLevel(mode);
    }

    public void onChange(MusicInfo music) {
        onPlay(music);
    }

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
            case R.id.iv_mode:
                switchPlayMode();
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


    private void onPlay(MusicInfo music) {
        if (music == null) {
            return;
        }
        setViewData(music);
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

    private void switchPlayMode() {
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                break;
            case SHUFFLE:
                mode = PlayModeEnum.ONE;
                break;
            case ONE:
                mode = PlayModeEnum.LOOP;
                break;
        }
        Preferences.savePlayMode(mode.value());
        initPlayMode();
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
        return SystemUtils.formatTime("mm:ss", time);
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
        }
    }
}
