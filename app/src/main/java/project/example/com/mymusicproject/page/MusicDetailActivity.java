package project.example.com.mymusicproject.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import project.example.com.mymusicproject.Constants;
import project.example.com.mymusicproject.R;
import project.example.com.mymusicproject.base.BaseActivity;
import project.example.com.mymusicproject.model.MusicInfo;

/**
 * 音乐播放详情界面
 *
 * @auther ccc
 * created at 2016/6/29 14:51
 ***/

public class MusicDetailActivity extends BaseActivity {

    @Bind(R.id.iv_play_page_bg)
    ImageView ivPlayPageBg;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_artist)
    TextView tvArtist;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.tv_current_time)
    TextView tvCurrentTime;
    @Bind(R.id.sb_progress)
    SeekBar sbProgress;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.iv_mode)
    ImageView ivMode;
    @Bind(R.id.iv_prev)
    ImageView ivPrev;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.iv_next)
    ImageView ivNext;
    @Bind(R.id.play_page_controller)
    LinearLayout playPageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        ButterKnife.bind(this);
        MusicInfo musicInfo = (MusicInfo)getIntent().getSerializableExtra(Constants.PARAM_DATA);

    }

    @Override
    protected void setListener() {

    }

    private void initTopBar() {
    }

    /*****************************************************************************************/
    public static Intent getIntent(Context context, MusicInfo musicInfo) {
        Intent intent = new Intent(context, MusicDetailActivity.class);
        intent.putExtra(Constants.PARAM_DATA, musicInfo);
        return intent;
    }
}
