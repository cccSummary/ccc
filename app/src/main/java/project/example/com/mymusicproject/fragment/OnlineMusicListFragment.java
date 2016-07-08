package project.example.com.mymusicproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.adapter.SongListAdapter;
import project.example.com.mymusicproject.base.BaseFragment;
import project.example.com.mymusicproject.model.SongListInfo;
import project.example.com.mymusicproject.page.OnLineMusicActivity;
import project.example.com.mymusicproject.util.NetworkUtils;

/**
 * 在线音乐
 * Created by wcy on 2015/11/26.
 */
public class OnlineMusicListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.lv_song_list)
    ListView lvSongList;
//    @Bind(R.id.ll_loading)
//    LinearLayout llLoading;
//    @Bind(R.id.ll_load_fail)
//    LinearLayout llLoadFail;
    private List<SongListInfo> mSongLists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_song_list, container, false);
    }

    @Override
    protected void init() {
        if (!NetworkUtils.isNetworkAvailable(getActivity())) {
//            ViewUtils.changeViewState(lvSongList, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
            return;
        }
        mSongLists = getPlayService().mSongLists;
        if (mSongLists.isEmpty()) {
            String[] titles = getResources().getStringArray(R.array.online_music_list_title);
            String[] types = getResources().getStringArray(R.array.online_music_list_type);
            for (int i = 0; i < titles.length; i++) {
                SongListInfo info = new SongListInfo();
                info.setTitle(titles[i]);
                info.setType(types[i]);
                mSongLists.add(info);
            }
        }
        SongListAdapter adapter = new SongListAdapter(mSongLists);
        lvSongList.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        lvSongList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SongListInfo songListInfo = mSongLists.get(position);
        Intent intent = new Intent(getActivity(), OnLineMusicActivity.class);
        intent.putExtra(Constants.PARAM_DATA, songListInfo);
        startActivity(intent);
    }
}
