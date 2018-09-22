package ecnu.ecnumusic;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.IBinder;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import Utils.MusicRequestUtil;
import Utils.ResultCallback;
import Utils.Utility;
import adapter.SingerSongRecyclerAdapter;
import classcollection.Music;
import classcollection.Song;
import fragments.BaseFragment;
import okhttp3.Request;
import okhttp3.Response;
import service.MusicService;
import shouyeclass.rank.SongData;

public class RankSongActivity extends BaseActivity implements SingerSongRecyclerAdapter.OnItemClickListener{
    private MusicService.MusicBinder musicBinder;
    private CollapsingToolbarLayout toolbarLayout;
    private ImageView iconImage;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private static final String TAG="RankSongActivity";
    private String icon,listNmae;
    private int topID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranksong);
        toolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toorbar);
        toolbar=(Toolbar)findViewById(R.id.singer_toorbar);
        iconImage=(ImageView)findViewById(R.id.icon_pic);
        recyclerView=(RecyclerView)findViewById(R.id.ranksong_recycler) ;
        recyclerView.setNestedScrollingEnabled(false);
        initWindows();
        initToolbar();
        Intent intent=getIntent();
        icon=intent.getStringExtra("icon");
        topID=intent.getIntExtra("topID",-1);
        listNmae=intent.getStringExtra("listName");
        Glide.with(this).load(icon).into(iconImage);
        toolbarLayout.setTitle(" ");
    }

    private void initWindows(){
        View decorView=getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void connection(IBinder service) {
        super.connection(service);
        musicBinder=(MusicService.MusicBinder)service;
        musicBinder.getService().setOnPlayEventListener(this);
        initRequest();
    }

    @Override
    public void onChange(Song song, Music music) {
        super.onChange(song,music);
    }

    @Override
    public void onPlayerStart() {
        super.onPlayerStart();
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void initRequest(){
        MusicRequestUtil.getRankSong(this, topID, new ResultCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseText=response.body().string();
                List<SongData> songDataList= Utility.handleRankSongResponse(responseText);
                LinearLayoutManager manager=new LinearLayoutManager(RankSongActivity.this);
                recyclerView.setLayoutManager(manager);
                SingerSongRecyclerAdapter adapter=new SingerSongRecyclerAdapter(songDataList,TAG);
                adapter.setOnItemClickListener(RankSongActivity.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onError(Request request, Exception ex) {
                Log.e(TAG, "onError: " );
            }
        });
    }

    @Override
    public void onItemClick(int position, List<Song> songs) {
        Log.e(TAG, "onItemClick: "+songs.get(position).songmid );
        musicBinder.playFromURL(position,songs);
    }
}
