package project.example.com.mymusicproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.bind.BindView;
import project.example.com.mymusicproject.model.JOnlineMusic;

/**
 * Created by Administrator on 2016/7/7.
 * 在线音乐列表适配器
 */
public class OnlineMusicAdapter extends BaseRecyclerAdapter<JOnlineMusic> {

    public OnlineMusicAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = inflater.inflate(R.layout.view_holder_music, parent, false);
        return new OnlineMusicHolder(layout, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    public class OnlineMusicHolder extends BaseRecyclerHolder<JOnlineMusic> {
        @BindView(id = R.id.iv_cover)
        ImageView ivCover;
        @BindView(id = R.id.tv_title)
        TextView tvTitle;
        @BindView(id = R.id.tv_artist)
        TextView tvArtist;
        @BindView(id = R.id.iv_more)
        ImageView ivMore;
        @BindView(id = R.id.v_divider)
        View vDivider;

        public OnlineMusicHolder(View view, final OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);

        }

        @Override
        public void recycle() {
            super.recycle();
        }

        @Override
        public void bind(Context context, int position, JOnlineMusic item) {
            tvTitle.setText("sdfghfhhhhhhhhhhhhhhhhhhhhhj");
            tvArtist.setText(item.getArtist_name());
//            ImageLoaderUtil.displayRoundImageByHandle(mTeamList.getIcon(), logo, R.drawable.icon_default);
        }
    }
}
