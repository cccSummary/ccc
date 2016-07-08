package project.example.com.mymusicproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.base.BaseFragment;
import project.example.com.mymusicproject.dragListView.DragAdapter;
import project.example.com.mymusicproject.dragListView.DragListView;
import project.example.com.mymusicproject.loader.MusicLoader;
import project.example.com.mymusicproject.model.MusicInfo;

/**
 * 本地音乐列表
 * Created by wcy on 2015/11/26.
 */
public class LocalMusicFragment extends BaseFragment implements ExpandableListView.OnChildClickListener {
    @Bind(R.id.expendlist)
    DragListView lvLocalMusic;
    @Bind(R.id.tv_empty)
    TextView tvEmpty;
    DragAdapter mAdapter;
    /***
     * 音乐数据集
     **/
    private ArrayList<MusicInfo> musicList;
    /**
     * 分组数据
     **/
    private List<String> group_list = new ArrayList<>();
    private Map<String, ArrayList<MusicInfo>> children;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_music, container, false);
    }

    @Override
    protected void init() {
        setGroupData();
        mAdapter = new DragAdapter(getActivity(), group_list, children);
        lvLocalMusic.setAdapter(mAdapter);
        if (getPlayService().getPlayingMusic() != null && getPlayService().getPlayingMusic().getType() == MusicInfo.Type.LOCAL) {
            lvLocalMusic.setSelection(getPlayService().getPlayingPosition());
        }
    }

    /**
     * 设置数据
     */
    public void setGroupData() {
        group_list.add("列表一");
        group_list.add("列表二");
        MusicLoader musicLoader = MusicLoader.instance(getActivity().getContentResolver());//获取音乐
        musicList = (ArrayList<MusicInfo>) musicLoader.getMusicList();
        setChildrenData();
    }

    /**
     * 设置子类目数据
     */
    private void setChildrenData() {
        children = Collections.synchronizedMap(new LinkedHashMap<String, ArrayList<MusicInfo>>());
        for (String s : group_list) {
            ArrayList<MusicInfo> array = new ArrayList<MusicInfo>();
            array.addAll(musicList);
            children.put(s, array);
        }

    }

    @Override
    protected void setListener() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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
        getPlayService().play(i1);
        return false;
    }
}
