package ecnu.ecnumusic;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import classcollection.mv.MvUrl;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import huqiang.dada.basecomponent.recyclerview.XRecyclerView;
import okhttp3.Request;
import okhttp3.Response;

public class PlayMVActivity extends AppCompatActivity {
    private JzvdStd std;
    private XRecyclerView rvMv;
    private String vid, pic, title;
    private List<MvUrl> mvUrls;
    private String url = "";

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
        vid = getIntent().getStringExtra("vid");
        Log.e("vid", "onCreate: " + vid);
        pic = getIntent().getStringExtra("pic");
        Log.e("pic", "onCreate: " + pic);
        title = getIntent().getStringExtra("title");
        initRequest();
        initRecycler();
    }

    private void initRecycler() {
        rvMv.setPullRefreshEnabled(false);
        rvMv.clearHeader();
        View view = View.inflate(this, R.layout.head_item_mv, null);
        rvMv.addHeaderView(view);
    }

    private void initRequest() {
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
                std.setUp(url, title, Jzvd.SCREEN_WINDOW_NORMAL);
                std.startVideo();
                Glide.with(PlayMVActivity.this).load(pic).into(std.thumbImageView);
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
}
