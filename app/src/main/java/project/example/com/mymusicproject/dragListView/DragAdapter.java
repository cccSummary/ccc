package project.example.com.mymusicproject.dragListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.loader.PictureLoader;
import project.example.com.mymusicproject.model.MusicInfo;
import project.example.com.mymusicproject.service.PlayService;

/**
 * Created by CongCong on 6/22/16.
 */
public class DragAdapter extends BaseExpandableListAdapter {

    private int selectedGroup;
    private int selectedChild;
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> groups;
    private Map<String, ArrayList<MusicInfo>> children;
    private List<String> group_list;
    private int mPlayingPosition;

    public DragAdapter(Context context, List<String> group_list, Map<String, ArrayList<MusicInfo>> children) {
        this.mContext = context;
        this.group_list = group_list;
        this.children = children;
        this.mInflater = LayoutInflater.from(context);
    }

    public void onPick(int[] position) {
        selectedGroup = position[0];
        selectedChild = position[1];
    }


    class GroupHolder {
        public TextView txt;
    }

    class ViewHolder {
        @Bind(R.id.albumPhoto)
        ImageView ivCover;
        @Bind(R.id.title)
        TextView tvTitle;
        @Bind(R.id.artist)
        TextView tvArtist;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void onDrop(int[] from, int[] to) {
        if (to[0] > children.size() || to[0] < 0 || to[1] < 0)
            return;
        MusicInfo tValue = getValue(from);
        children.get(children.keySet().toArray()[from[0]]).remove(tValue);
        children.get(children.keySet().toArray()[to[0]]).add(to[1], tValue);
        selectedGroup = -1;
        selectedChild = -1;
        notifyDataSetChanged();
    }

    private MusicInfo getValue(int[] id) {
        return (MusicInfo) children.get(children.keySet().toArray()[id[0]]).get(id[1]);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(children.keySet().toArray()[groupPosition]).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder itemHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.music_item, null);
            itemHolder = new ViewHolder(convertView);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolder) convertView.getTag();
        }
        mPlayingPosition = childPosition;
        MusicInfo music = (MusicInfo) getChild(groupPosition, childPosition);
        //1.是否载入默认 2.是否小图
//        ImageLoader.getInstance().displayImage(music.getAlbum(), itemHolder.ivCover, R.drawable.default_cover);
        Bitmap cover = PictureLoader.getArtwork(mContext, music.getId(), music.getAlbumId(), true, false);
        if (cover == null) {
            itemHolder.ivCover.setImageResource(R.drawable.default_cover);
        } else {
            itemHolder.ivCover.setImageBitmap(cover);
        }
//
////        //1.是否载入默认 2.是否小图
////        Bitmap bm = PictureLoader.getArtwork(mContext, music.getId(), music.getAlbumId(), true, false);

        itemHolder.tvTitle.setText(music.getTitle());
        itemHolder.tvArtist.setText(music.getArtist());
        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(children.keySet().toArray()[groupPosition]).size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return group_list.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return group_list.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_expandablelistview, null);
            groupHolder = new GroupHolder();
            groupHolder.txt = (TextView) convertView.findViewById(R.id.item_expandablelistview_txet);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.txt.setText(group_list.get(groupPosition));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 更新播放位置
     *
     * @param playService
     */
    public void updatePlayingPosition(PlayService playService) {
        if (playService.getPlayingMusic() != null) {
            mPlayingPosition = playService.getPlayingPosition();
        } else {
            mPlayingPosition = -1;
        }
    }
}
