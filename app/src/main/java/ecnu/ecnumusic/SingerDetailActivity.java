package ecnu.ecnumusic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import adapter.SingerFragmentAdapter;
import fragments.SingerAlbumFragment;
import fragments.SingerMVFragment;
import fragments.SingerSongFragment;
import service.MusicService;

public class SingerDetailActivity extends BaseActivity {

    private String singerMid, singerPic, singerName;
    private ImageView singerImage;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MusicService.MusicBinder musicBinder;
    private CollapsingToolbarLayout toolbarLayout;
    private Toolbar toolbar;
    private static final String TAG = "SingerDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singerdetail);
        viewPager = findViewById(R.id.singer_viewpager);
        tabLayout = findViewById(R.id.singer_tablayout);
        toolbarLayout = findViewById(R.id.collapsing_toorbar);
        toolbar = findViewById(R.id.singer_toorbar);

        Intent intent = getIntent();
        singerMid = intent.getStringExtra("singerMid");
        singerPic = intent.getStringExtra("singerPic");
        singerName = intent.getStringExtra("singerName");
        singerImage = findViewById(R.id.singer_pic);
        toolbarLayout.setTitle(singerName);
        Glide.with(this).load(singerPic).into(singerImage);
        initWindows();
        initToolbar();
        initViewPager();
    }

    @Override
    public void connection(IBinder service) {
        super.connection(service);
        musicBinder=(MusicService.MusicBinder)service;


    }

    private void initViewPager(){
        Log.e(TAG, "initViewPager: " );
        List<Fragment> fragmentList=new ArrayList<>();
        List<String> titleList=new ArrayList<>();

        fragmentList.add(new SingerSongFragment());
        fragmentList.add(new SingerAlbumFragment());
        fragmentList.add(new SingerMVFragment());

        titleList.add("歌曲");
        titleList.add("专辑");
        titleList.add("MV");
        //  titleList.add("详情");
        SingerFragmentAdapter adapter = new SingerFragmentAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initWindows() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public String getSingerMid() {
        return singerMid;
    }

    public MusicService.MusicBinder getMusicBinder() {
        return musicBinder;
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_alpha_out_from_center);
    }
}
