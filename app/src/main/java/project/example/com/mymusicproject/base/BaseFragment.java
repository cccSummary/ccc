package project.example.com.mymusicproject.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;

/**
 * 基类
 */
public abstract class BaseFragment extends Fragment {
    protected Handler mHandler;
    private boolean mResumed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        if (activity instanceof MusicActivity) {
//            mPlayService = ((MusicActivity) activity).getPlayService();
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        init();
        setListener();
        mResumed = true;
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract void init();

    protected abstract void setListener();

    public boolean isResume() {
        return mResumed;
    }


}
