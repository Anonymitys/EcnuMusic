package ecnu.ecnumusic;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.OtherMvAdapter;
import classcollection.Singer;
import classcollection.mv.MVInfo;
import classcollection.mv.MvUrl;
import classcollection.mv.OtherMv;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import fragments.BaseFragment;
import huqiang.dada.basecomponent.recyclerview.XRecyclerView;
import okhttp3.Request;
import okhttp3.Response;

public class PlayMVActivity extends BaseActivity implements OtherMvAdapter.OnItemClickListener {
    private JzvdStd std;
    private XRecyclerView rvMv;
    private String vid, pic, title;
    private List<MvUrl> mvUrls;
    private List<OtherMv> otherMvs;
    private String url = "";
    private TextView tvName, tvSinger;
    private ExpandableTextView expandableTextView;
    private OtherMvAdapter adapter;

    public static void getLaunch(Activity activity, String vid, String pic, String title) {
        Intent intent = new Intent(activity, PlayMVActivity.class);
        intent.putExtra("vid", vid);
        intent.putExtra("pic", pic);
        intent.putExtra("title", title);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_alpha_in_from_center, R.anim.animation_static);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mv);
        std = findViewById(R.id.video_player);
        rvMv = findViewById(R.id.rv_mv);
        expandableTextView = findViewById(R.id.expand_text_view);
        tvName = findViewById(R.id.name);
        tvSinger = findViewById(R.id.singer);
        vid = getIntent().getStringExtra("vid");
        pic = getIntent().getStringExtra("pic");
        title = getIntent().getStringExtra("title");
        initRequest(vid, true);
        initRecycler();
    }

    private void initRecycler() {
        rvMv.setPullRefreshEnabled(false);
        rvMv.clearHeader();
        rvMv.clearFooter();
       // rvMv.noMoreLoading();
        rvMv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initRequest(String vid, boolean isFirst) {
        MusicRequestUtil.getMvurl(this, vid, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                mvUrls = Utility.handleMvUrlResponse(str, vid);
                for (int i = 4; i > 0; i--) {
                    Log.e("size", "onResponse: " + mvUrls.get(i).mvurlList.size());
                    if (mvUrls.get(i).mvurlList.size() > 0) {
                        url = mvUrls.get(i).mvurlList.get(0);
                        Log.e("mvUrl", "onResponse: " + mvUrls.get(i).mvurlList.get(0));
                        break;
                    }
                }
                Glide.with(PlayMVActivity.this).load(pic).into(std.thumbImageView);
                std.setUp(url, title, Jzvd.SCREEN_WINDOW_NORMAL);
                std.startVideo();
            }

            @Override
            public void onError(Request request, Exception ex) {
                Log.e("huqiang", "onError: ");
            }
        });

        MusicRequestUtil.getMvInfo(this, vid, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                MVInfo info = Utility.handleMvInfoResponse(str, vid);
                Log.e("info", "onResponse: " + info.name);
                // List<OtherMv> mvs = Utility.handleOtherMvResponse(str);
                otherMvs = Utility.handleOtherMvResponse(str);
                Log.e("othermv", "onResponse: " + otherMvs.size());
                if (isFirst) {
                    //otherMvs.addAll(mvs);
                    adapter = new OtherMvAdapter(otherMvs);
                    adapter.setOnOtherMvClickListener(PlayMVActivity.this);
                    rvMv.setAdapter(adapter);
                } else {
                    adapter.addItem(otherMvs);
                    rvMv.scrollToPosition(0);
                }


            }

            @Override
            public void onError(Request request, Exception ex) {
                Log.e("huqiang", "onError: ");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Jzvd.clearSavedProgress(this, url);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_alpha_out_from_center);
    }


    @Override
    public void onItemClick(String vid, String name) {
        title = name;
        initRequest(vid, false);
    }

    @Override
    public boolean usePlaybarView() {
        return false;
    }
}
