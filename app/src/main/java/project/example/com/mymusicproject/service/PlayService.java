package project.example.com.mymusicproject.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.base.BaseActivity;
import project.example.com.mymusicproject.enums.PlayModeEnum;
import project.example.com.mymusicproject.loader.MusicLoader;
import project.example.com.mymusicproject.model.MusicInfo;
import project.example.com.mymusicproject.model.SongListInfo;
import project.example.com.mymusicproject.util.Preferences;

/**
 * 音乐播放service
 *
 * @auther ccc
 * created at 2016/7
 ***/

public class PlayService extends Service implements MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {
    private static final long TIME_UPDATE = 100L;
    private static final List<BaseActivity> sActivityStack = new ArrayList<>();
    // 本地歌曲列表
    private List<MusicInfo> sMusicList = new ArrayList<>();
    private MediaPlayer mPlayer = new MediaPlayer();
    private Handler mHandler = new Handler();
    private AudioManager mAudioManager;
    public List<SongListInfo> mSongLists = new ArrayList<>();
    private OnPlayerEventListener mListener;
    // 正在播放的歌曲
    private MusicInfo mPlayingMusic;
    // 正在播放的本地歌曲的序号
    private int mPlayingPosition;
    private boolean isPause = false;
    private long quitTimerRemain;
    private Runnable mBackgroundRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying() && mListener != null) {
                mListener.onPublish(mPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };
    private Runnable mQuitRunnable = new Runnable() {
        @Override
        public void run() {
            quitTimerRemain -= DateUtils.SECOND_IN_MILLIS;
            if (quitTimerRemain > 0) {
                if (mListener != null) {
                    mListener.onTimer(quitTimerRemain);
                }
                mHandler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
            } else {
                stop();
            }
        }
    };

    public static void addToStack(BaseActivity activity) {
        sActivityStack.add(activity);
    }

    public static void removeFromStack(BaseActivity activity) {
        sActivityStack.remove(activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updateMusicList();
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mPlayer.setOnCompletionListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }
        switch (intent.getAction()) {
            case Constants.ACTION_MEDIA_PLAY_PAUSE:
                playPause();
                break;
            case Constants.ACTION_MEDIA_NEXT:
                next();
                break;
            case Constants.ACTION_MEDIA_PREVIOUS:
                prev();
                break;
        }
        return START_NOT_STICKY;
    }

    public List<MusicInfo> getMusicList() {
        return sMusicList;
    }

    /**
     * 每次启动时扫描音乐
     */
    public void updateMusicList() {
        MusicLoader musicLoader = MusicLoader.instance(getContentResolver());//获取音乐
        sMusicList = (ArrayList<MusicInfo>) musicLoader.getMusicList();
        if (getMusicList().isEmpty()) {
            return;
        }
        mPlayingMusic = mPlayingMusic == null ? getMusicList().get(mPlayingPosition) : mPlayingMusic;
    }

    /**
     * 顺序播放
     **/
    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    public void setOnPlayEventListener(OnPlayerEventListener listener) {
        mListener = listener;
    }

    /**
     * 点击播放
     **/
    public int play(int position) {
        if (getMusicList().isEmpty()) {
            return -1;
        }
        if (position < 0) {
            position = getMusicList().size() - 1;
        } else if (position >= getMusicList().size()) {
            position = 0;
        }
        mPlayingPosition = position;
        mPlayingMusic = getMusicList().get(mPlayingPosition);

        try {
            mPlayer.reset();
            mPlayer.setDataSource(mPlayingMusic.getUrl());
            mPlayer.prepare();
            start();
            if (mListener != null) {
                mListener.onChange(mPlayingMusic);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Preferences.saveCurrentSongId(mPlayingMusic.getId());
        return mPlayingPosition;
    }

    /**
     * 暂停和播放按钮
     **/
    public void playPause() {
        if (isPlaying()) {
            pause();
        } else if (isPause()) {
            resume();
        } else {
            play(getPlayingPosition());
        }
    }

    private void start() {
        mPlayer.start();
        isPause = false;
        mHandler.post(mBackgroundRunnable);
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    /**
     * 暂停
     **/
    public int pause() {
        if (!isPlaying()) {
            return -1;
        }
        mPlayer.pause();
        isPause = true;
        mHandler.removeCallbacks(mBackgroundRunnable);
        mAudioManager.abandonAudioFocus(this);
        if (mListener != null) {
            mListener.onPlayerPause();
        }
        return mPlayingPosition;
    }

    public int resume() {
        if (isPlaying()) {
            return -1;
        }
        start();
        if (mListener != null) {
            mListener.onPlayerResume();
        }
        return mPlayingPosition;
    }

    /**
     * 下一曲
     **/
    public int next() {
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case LOOP:
                return play(mPlayingPosition + 1);
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(getMusicList().size());
                return play(mPlayingPosition);
            case ONE:
                return play(mPlayingPosition);
            default:
                return play(mPlayingPosition + 1);
        }
    }

    /**
     * 上一曲
     **/
    public int prev() {
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case LOOP:
                return play(mPlayingPosition - 1);
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(getMusicList().size());
                return play(mPlayingPosition);
            case ONE:
                return play(mPlayingPosition);
            default:
                return play(mPlayingPosition - 1);
        }
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPause()) {
            mPlayer.seekTo(msec);
            if (mListener != null) {
                mListener.onPublish(msec);
            }
        }
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    public boolean isPause() {
        return mPlayer != null && isPause;
    }

    /**
     * 获取正在播放的本地歌曲的序号
     */
    public int getPlayingPosition() {
        return mPlayingPosition;
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public MusicInfo getPlayingMusic() {
        return mPlayingMusic;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (isPlaying()) {
                    pause();
                }
                break;
        }
    }


    private void stopQuitTimer() {
        mHandler.removeCallbacks(mQuitRunnable);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mListener = null;
        return true;
    }

    public void stop() {
        pause();
        stopQuitTimer();
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        stopSelf();
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }
}
