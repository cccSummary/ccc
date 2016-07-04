package project.example.com.mymusicproject.page;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.base.BaseActivity;
import project.example.com.mymusicproject.fragment.MusicPlayFragment;
import project.example.com.mymusicproject.service.PlayService;

/**
*
*@auther ccc
*created at 2016/6/21 10:43
***/

public class MusicPlayActivity extends BaseActivity {
    private MusicPlayFragment mPlayFragment;
    private PlayService mPlayService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService();
        initView();
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

    public PlayService getPlayService() {
        return mPlayService;
    }

    private void initView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new MusicPlayFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commit();
    }

    @Override
    protected void setListener() {
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MusicPlayActivity.class);
        return intent;
    }
}
