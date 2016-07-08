package project.example.com.mymusicproject.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.adapter.OnlineMusicAdapter;
import project.example.com.mymusicproject.base.BaseActivity;
import project.example.com.mymusicproject.model.JOnlineMusic;
import project.example.com.mymusicproject.net.MusicListLoad;

/**
 * Created by Administrator on 2016/7/7.
 */
public class OnLineMusicActivity extends BaseActivity implements AdapterViewCompat.OnItemClickListener {
    @Bind(R.id.recyclerView)
    RecyclerView mRecycleview;
    OnlineMusicAdapter mAdapter;
    List<JOnlineMusic> jOnlineMusics = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music_layout);
        initView();
    }

    private void initView() {
        // 线性布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // 设置布局管理器
        mRecycleview.setLayoutManager(linearLayoutManager);
        mAdapter = new OnlineMusicAdapter(OnLineMusicActivity.this);
        mAdapter.setData(jOnlineMusics);
        mRecycleview.setAdapter(mAdapter);
        initData();

    }

    private void initData() {
        new MusicListLoad("") {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onFinish(@Nullable List<JOnlineMusic> onlineMusics) {
//                jOnlineMusics = onlineMusics;\
                jOnlineMusics.addAll(onlineMusics);
//                mAdapter.setData(onlineMusics);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterViewCompat<?> parent, View view, int position, long id) {

    }

    @Override
    protected void setListener() {

    }
}
