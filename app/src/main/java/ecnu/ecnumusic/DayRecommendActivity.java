package ecnu.ecnumusic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import Utils.GlideImgManager;
import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.SongDetailAdapter;
import classcollection.CdList;
import classcollection.Song;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Request;
import okhttp3.Response;
import service.MusicService;

public class DayRecommendActivity extends BaseActivity implements SongDetailAdapter.OnItemClickListener {
    public static final String TAG = "DayRecommnedFragment";
    private Long content_id;
    private RecyclerView recyclerView;
    private View loadingView;
    private AnimationDrawable mAnimationDrawable;
    private TextView titleView;
    private ImageView title_image, title_image_background;
    private String title, imageUrl;
    private android.support.v7.widget.Toolbar toolbar;
    private MusicService.MusicBinder musicBinder;
    private Handler handler = new Handler();

    public static void getLaunch(Activity activity, Long content_id, String title, String imageUrl) {
        Intent intent = new Intent(activity, DayRecommendActivity.class);
        intent.putExtra("content_id", content_id);
        intent.putExtra("title", title);
        intent.putExtra("imageUrl", imageUrl);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_alpha_in_from_center, R.anim.animation_static);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_recommend);
        recyclerView = findViewById(R.id.day_recommend);
        recyclerView.setNestedScrollingEnabled(false);
        titleView = findViewById(R.id.title_content);
        title_image = findViewById(R.id.title_image);
        toolbar = findViewById(R.id.day_toolbar);
        title_image_background = findViewById(R.id.title_imagebackground);
        initToolBar();
        content_id = getIntent().getLongExtra("content_id", 0);
        title = getIntent().getStringExtra("title");
        imageUrl = getIntent().getStringExtra("imageUrl");

        loadingView = ((ViewStub) findViewById(R.id.vs_loading)).inflate();
        ImageView img = loadingView.findViewById(R.id.img_progress);
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        showLoading();
        GlideImgManager.glideLoaderCircle(this, imageUrl, R.drawable.album, R.drawable.album,
                title_image);
        Glide.with(this).load(imageUrl)
                .error(R.drawable.stackblur_default)
                .bitmapTransform(new BlurTransformation(this, 70, 4)).into(title_image_background);
        titleView.setText(title);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        getDayRecommend(String.valueOf(content_id));


    }

    private void initToolBar() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("新歌推荐");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getDayRecommend(String dissid) {
        MusicRequestUtil.getDetailMusic(this, dissid, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String textString = response.body().string();
                CdList list = Utility.handleCdlistResponse(textString);
                SongDetailAdapter adapter = new SongDetailAdapter(list.songlist, "DayRecommend");
                adapter.setOnItemClickListener(DayRecommendActivity.this);
                recyclerView.setAdapter(adapter);
                showContent();

            }

            @Override
            public void onError(Request request, Exception ex) {
                Toast.makeText(DayRecommendActivity.this, "dayRecommend failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void connection(IBinder service) {
        super.connection(service);
        musicBinder = (MusicService.MusicBinder) service;
    }

    @Override
    public void onItemClick(int position, List<Song> songs) {
        musicBinder.playFromvkey(position, songs);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_alpha_out_from_center);
    }

    public void showLoading() {
        if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
            loadingView.setVisibility(View.VISIBLE);
        }
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
    }

    public void showContent() {
        if (loadingView != null && loadingView.getVisibility() != View.GONE) {
            loadingView.setVisibility(View.GONE);
        }
        if (mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
    }
}
